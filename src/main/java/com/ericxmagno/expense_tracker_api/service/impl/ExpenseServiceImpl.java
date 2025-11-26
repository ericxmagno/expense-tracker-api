package com.ericxmagno.expense_tracker_api.service.impl;

import com.ericxmagno.expense_tracker_api.dto.ExpenseRequest;
import com.ericxmagno.expense_tracker_api.dto.SummaryResponse;
import com.ericxmagno.expense_tracker_api.exception.ExpenseNotFoundException;
import com.ericxmagno.expense_tracker_api.exception.ExpenseValidationException;
import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import com.ericxmagno.expense_tracker_api.repository.ExpenseRepository;
import com.ericxmagno.expense_tracker_api.service.ExpenseService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

  private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);
  private final ExpenseRepository repository;
  private final ModelMapper modelMapper;

  @Override
  public Expenses addExpense(ExpenseRequest request) {
    Expenses expense = modelMapper.map(request, Expenses.class);
    try {
      return repository.save(expense);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error saving Expenses {} to database. ", expense.getTitle(), e);
      throw new ExpenseValidationException(
          "Expenses data violates database constraints (e.g., unique constraint)", e);
    } catch (Exception e) {
      logger.error("Error saving Expenses {} to database. ", expense.getTitle(), e);
      throw e;
    }
  }

  @Override
  public Expenses getExpenseById(Long id) throws ExpenseNotFoundException {
    return repository
        .findById(id)
        .orElseThrow(() -> new ExpenseNotFoundException("Expenses with id {} not found."));
  }

  @Override
  public Page<Expenses> listExpenses(
      Pageable pageable, Category category, LocalDate start, LocalDate end) {
    if (category != null) return repository.findByCategory(category, pageable);
    if (start != null && end != null) return repository.findByDateBetween(start, end, pageable);
    return repository.findAll(pageable);
  }

  @Override
  public Expenses updateExpense(Long id, ExpenseRequest request) {
    Expenses existing = getExpenseById(id);

    existing.setTitle(request.getTitle());
    existing.setDate(request.getDate());
    existing.setAmount(request.getAmount());
    existing.setCategory(request.getCategory());
    existing.setDescription(request.getDescription());

    try {
      return repository.save(existing);
    } catch (DataIntegrityViolationException e) {
      logger.error("Error saving Expenses {} to database. ", existing.getId(), e);
      throw new ExpenseValidationException(
          "Expenses data violates database constraints (e.g., unique constraint)", e);
    } catch (Exception e) {
      logger.error("Error saving Expenses {} to database. ", existing.getId(), e);
      throw e;
    }
  }

  @Override
  public void deleteExpense(Long id) {
    Expenses existing = getExpenseById(id);
    repository.delete(existing);
  }

  @Override
  public SummaryResponse monthlySummary(String month) {
    YearMonth ym = YearMonth.parse(month);
    LocalDate start = ym.atDay(1);
    LocalDate end = ym.atEndOfMonth();

    List<Expenses> expenseList = repository.findByDateBetween(start, end);

    BigDecimal total =
        expenseList.stream().map(Expenses::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

    return SummaryResponse.builder().month(month).totalSpent(total).build();
  }
}
