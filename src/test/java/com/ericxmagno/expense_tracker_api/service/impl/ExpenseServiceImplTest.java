package com.ericxmagno.expense_tracker_api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ericxmagno.expense_tracker_api.dto.ExpenseRequest;
import com.ericxmagno.expense_tracker_api.dto.SummaryResponse;
import com.ericxmagno.expense_tracker_api.exception.ExpenseNotFoundException;
import com.ericxmagno.expense_tracker_api.exception.ExpenseValidationException;
import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import com.ericxmagno.expense_tracker_api.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class ExpenseServiceImplTest {

  @Mock private ExpenseRepository repository;

  @Mock private ModelMapper modelMapper;

  @InjectMocks private ExpenseServiceImpl service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddExpenseSuccess() {
    ExpenseRequest request = new ExpenseRequest();
    request.setTitle("Lunch");
    request.setCategory(Category.FOOD);
    request.setAmount(BigDecimal.TEN);
    request.setDate(LocalDate.now());

    Expenses mappedExpense = new Expenses();
    mappedExpense.setTitle("Lunch");

    when(modelMapper.map(request, Expenses.class)).thenReturn(mappedExpense);
    when(repository.save(mappedExpense)).thenReturn(mappedExpense);

    Expenses result = service.addExpense(request);

    assertNotNull(result);
    assertEquals("Lunch", result.getTitle());
    verify(repository, times(1)).save(mappedExpense);
  }

  @Test
  void testAddExpenseThrowsValidationException() {
    ExpenseRequest request = new ExpenseRequest();
    Expenses mappedExpense = new Expenses();

    when(modelMapper.map(request, Expenses.class)).thenReturn(mappedExpense);
    when(repository.save(mappedExpense)).thenThrow(DataIntegrityViolationException.class);

    assertThrows(ExpenseValidationException.class, () -> service.addExpense(request));
  }

  @Test
  void testAddExpenseThrowsGenericException() {
    ExpenseRequest request = new ExpenseRequest();
    Expenses mappedExpense = new Expenses();

    when(modelMapper.map(request, Expenses.class)).thenReturn(mappedExpense);
    when(repository.save(mappedExpense)).thenThrow(RuntimeException.class);

    assertThrows(RuntimeException.class, () -> service.addExpense(request));
  }

  @Test
  void testGetExpenseByIdExists() {
    Expenses expense = new Expenses();
    expense.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(expense));

    Expenses result = service.getExpenseById(1L);

    assertEquals(1L, result.getId());
  }

  @Test
  void testGetExpenseByIdNotFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ExpenseNotFoundException.class, () -> service.getExpenseById(1L));
  }

  @Test
  void testUpdateExpense() {
    Expenses existing = new Expenses();
    existing.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenReturn(existing);

    ExpenseRequest request = new ExpenseRequest();
    request.setTitle("Dinner");
    request.setAmount(BigDecimal.valueOf(20));
    request.setDate(LocalDate.now());
    request.setCategory(Category.FOOD);

    Expenses result = service.updateExpense(1L, request);

    assertEquals("Dinner", result.getTitle());
    verify(repository, times(1)).save(existing);
  }

  @Test
  void testUpdateExpenseThrowDataIntegrityViolationException() {
    Expenses existing = new Expenses();
    existing.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(existing));
    when(repository.save(existing))
        .thenThrow(new DataIntegrityViolationException("Invalid fields."));

    ExpenseRequest request = new ExpenseRequest();
    request.setTitle("Dinner");
    request.setAmount(BigDecimal.valueOf(20));
    request.setDate(LocalDate.now());
    request.setCategory(Category.FOOD);

    assertThrows(ExpenseValidationException.class, () -> service.updateExpense(1L, request));
    verify(repository, times(1)).save(existing);
  }

  @Test
  void testUpdateExpenseThrowGenericException() {
    Expenses existing = new Expenses();
    existing.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(existing));
    when(repository.save(existing)).thenThrow(new RuntimeException("Generic exception"));

    ExpenseRequest request = new ExpenseRequest();
    request.setTitle("Dinner");
    request.setAmount(BigDecimal.valueOf(20));
    request.setDate(LocalDate.now());
    request.setCategory(Category.FOOD);

    assertThrows(RuntimeException.class, () -> service.updateExpense(1L, request));
    verify(repository, times(1)).save(existing);
  }

  @Test
  void testDeleteExpense() {
    Expenses existing = new Expenses();
    existing.setId(1L);
    when(repository.findById(1L)).thenReturn(Optional.of(existing));

    service.deleteExpense(1L);

    verify(repository, times(1)).delete(existing);
  }

  @Test
  void testMonthlySummary() {
    Expenses e1 = new Expenses();
    e1.setAmount(BigDecimal.TEN);
    Expenses e2 = new Expenses();
    e2.setAmount(BigDecimal.valueOf(20));

    LocalDate start = LocalDate.of(2025, 11, 1);
    LocalDate end = LocalDate.of(2025, 11, 30);

    when(repository.findByDateBetween(start, end)).thenReturn(Arrays.asList(e1, e2));

    SummaryResponse summary = service.monthlySummary("2025-11");

    assertEquals("2025-11", summary.getMonth());
    assertEquals(BigDecimal.valueOf(30), summary.getTotalSpent());
  }

  @Test
  void testListExpensesByCategory() {
    Category cat = Category.FOOD;
    Page<Expenses> page = new PageImpl<>(List.of(new Expenses()));
    when(repository.findByCategory(cat, PageRequest.of(0, 10))).thenReturn(page);

    Page<Expenses> result = service.listExpenses(PageRequest.of(0, 10), cat, null, null);

    assertEquals(1, result.getTotalElements());
  }

  @Test
  void testListExpensesByDate() {
    LocalDate start = LocalDate.now();
    LocalDate end = LocalDate.now().plusDays(1);
    Page<Expenses> page = new PageImpl<>(List.of(new Expenses()));
    when(repository.findByDateBetween(start, end, PageRequest.of(0, 10))).thenReturn(page);

    Page<Expenses> result = service.listExpenses(PageRequest.of(0, 10), null, start, end);

    assertEquals(1, result.getTotalElements());
  }

  @Test
  void testListAllExpenses() {
    Page<Expenses> page = new PageImpl<>(List.of(new Expenses()));
    when(repository.findAll(PageRequest.of(0, 10))).thenReturn(page);

    Page<Expenses> result = service.listExpenses(PageRequest.of(0, 10), null, null, null);

    assertEquals(1, result.getTotalElements());
  }
}
