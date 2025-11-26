package com.ericxmagno.expense_tracker_api.controller;

import com.ericxmagno.expense_tracker_api.dto.ExpenseRequest;
import com.ericxmagno.expense_tracker_api.dto.ExpenseResponse;
import com.ericxmagno.expense_tracker_api.dto.SummaryResponse;
import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import com.ericxmagno.expense_tracker_api.service.ExpenseService;
import com.ericxmagno.expense_tracker_api.sort.SortableFields;
import com.ericxmagno.expense_tracker_api.util.MapperUtil;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

  private final ExpenseService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest request) {
    Expenses savedExpense = service.addExpense(request);
    return ResponseEntity.status(201).body(MapperUtil.createResponse(savedExpense));
  }

  @GetMapping
  public ResponseEntity<Page<ExpenseResponse>> listExpenses(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(required = false) Category category,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate start,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
      @RequestParam(defaultValue = "date") String sortBy,
      @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

    SortableFields sortField = SortableFields.fromString(sortBy);
    Sort sort = Sort.by(direction, sortField.field());
    PageRequest pageable = PageRequest.of(page, size, sort);

    return ResponseEntity.ok(
        MapperUtil.createResponse(service.listExpenses(pageable, category, start, end)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
    return ResponseEntity.ok(MapperUtil.createResponse(service.getExpenseById(id)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ExpenseResponse> updateExpense(
      @PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
    return ResponseEntity.ok(MapperUtil.createResponse(service.updateExpense(id, request)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
    service.deleteExpense(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/summary")
  public ResponseEntity<SummaryResponse> getSummary(@RequestParam String month) {

    return ResponseEntity.ok(service.monthlySummary(month));
  }
}
