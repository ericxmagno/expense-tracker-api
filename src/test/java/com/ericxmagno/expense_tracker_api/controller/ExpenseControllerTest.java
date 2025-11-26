package com.ericxmagno.expense_tracker_api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ericxmagno.expense_tracker_api.dto.ExpenseRequest;
import com.ericxmagno.expense_tracker_api.dto.ExpenseResponse;
import com.ericxmagno.expense_tracker_api.dto.SummaryResponse;
import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import com.ericxmagno.expense_tracker_api.service.ExpenseService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ExpenseControllerTest {

  @InjectMocks private ExpenseController controller;

  @Mock private ExpenseService service;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // Helper method to create a dummy Expense
  private Expenses createExpense(Long id, String title, BigDecimal amount) {
    Expenses e = new Expenses();
    e.setId(id);
    e.setTitle(title);
    e.setAmount(amount);
    e.setCategory(Category.FOOD);
    e.setDate(LocalDate.now());
    return e;
  }

  @Test
  void testAddExpense() {
    ExpenseRequest request = new ExpenseRequest();
    request.setTitle("Lunch");
    request.setAmount(new BigDecimal("12.5"));
    request.setCategory(Category.FOOD);

    Expenses saved = createExpense(1L, "Lunch", new BigDecimal("12.5"));

    when(service.addExpense(request)).thenReturn(saved);

    ResponseEntity<ExpenseResponse> response = controller.addExpense(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assert response.getBody() != null;
    assertEquals(saved.getTitle(), response.getBody().getTitle());

    verify(service, times(1)).addExpense(request);
  }

  @Test
  void testListExpenses() {
    Expenses e1 = createExpense(1L, "Lunch", new BigDecimal("12.5"));
    Expenses e2 = createExpense(2L, "Dinner", new BigDecimal("20.0"));

    Page<Expenses> page = new PageImpl<>(List.of(e1, e2));

    when(service.listExpenses(any(Pageable.class), any(), any(), any())).thenReturn(page);

    ResponseEntity<Page<ExpenseResponse>> response =
        controller.listExpenses(0, 20, null, null, null, "date", Sort.Direction.DESC);

    assert response.getBody() != null;
    assertEquals(2, response.getBody().getTotalElements());
    verify(service, times(1)).listExpenses(any(Pageable.class), any(), any(), any());
  }

  @Test
  void testGetExpenseById() {
    Expenses e = createExpense(1L, "Lunch", new BigDecimal("12.5"));
    when(service.getExpenseById(1L)).thenReturn(e);

    ResponseEntity<ExpenseResponse> response = controller.getExpenseById(1L);

    assertEquals("Lunch", response.getBody().getTitle());
    verify(service).getExpenseById(1L);
  }

  @Test
  void testDeleteExpense() {
    doNothing().when(service).deleteExpense(1L);

    ResponseEntity<Void> response = controller.deleteExpense(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(service).deleteExpense(1L);
  }

  @Test
  void testUpdateExpense() {
    ExpenseRequest request = new ExpenseRequest();
    request.setTitle("Lunch");
    request.setAmount(new BigDecimal("12.5"));
    request.setCategory(Category.FOOD);

    Expenses updated = createExpense(1L, "Not Lunch", new BigDecimal("12.5"));

    when(service.updateExpense(1L, request)).thenReturn(updated);

    ResponseEntity<ExpenseResponse> response = controller.updateExpense(1L, request);

    assertEquals("Not Lunch", response.getBody().getTitle());
    verify(service).updateExpense(1L, request);
  }

  @Test
  void testGetSummary() {
    // Suppose your service returns a dummy SummaryResponse
    SummaryResponse summary = SummaryResponse.builder().totalSpent(new BigDecimal("100.0")).build();

    when(service.monthlySummary("2025-11")).thenReturn(summary);

    ResponseEntity<SummaryResponse> response = controller.getSummary("2025-11");

    assert response.getBody() != null;
    assertEquals(new BigDecimal("100.0"), response.getBody().getTotalSpent());
    verify(service).monthlySummary("2025-11");
  }
}
