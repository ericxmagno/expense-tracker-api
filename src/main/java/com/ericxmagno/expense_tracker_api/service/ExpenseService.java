package com.ericxmagno.expense_tracker_api.service;

import com.ericxmagno.expense_tracker_api.dto.ExpenseRequest;
import com.ericxmagno.expense_tracker_api.dto.SummaryResponse;
import com.ericxmagno.expense_tracker_api.exception.ExpenseNotFoundException;
import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {

  Expenses addExpense(ExpenseRequest request);

  Expenses getExpenseById(Long id) throws ExpenseNotFoundException;

  Page<Expenses> listExpenses(Pageable pageable, Category category, LocalDate start, LocalDate end);

  Expenses updateExpense(@NotNull Long id, @Valid ExpenseRequest request);

  void deleteExpense(Long id);

  SummaryResponse monthlySummary(String month);
}
