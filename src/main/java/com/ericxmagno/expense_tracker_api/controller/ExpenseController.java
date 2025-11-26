package com.ericxmagno.expense_tracker_api.controller;

import com.ericxmagno.expense_tracker_api.dto.ExpenseRequest;
import com.ericxmagno.expense_tracker_api.dto.ExpenseResponse;
import com.ericxmagno.expense_tracker_api.dto.SummaryResponse;
import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import com.ericxmagno.expense_tracker_api.service.ExpenseService;
import com.ericxmagno.expense_tracker_api.util.MapperUtils;
import com.ericxmagno.expense_tracker_api.util.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

  private final ExpenseService service;

  @Operation(summary = "Create a new expense")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Expense created",
        content = @Content(schema = @Schema(implementation = ExpenseResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid request")
  })
  @PostMapping
  public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody ExpenseRequest request) {
    Expenses savedExpense = service.addExpense(request);
    return ResponseEntity.status(201).body(MapperUtils.createResponse(savedExpense));
  }

  @Operation(summary = "List expenses with pagination and optional filters")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "List of expenses retrieved")})
  @GetMapping
  public ResponseEntity<Page<ExpenseResponse>> listExpenses(
      @Parameter(description = "Page number filter") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Max size of response") @RequestParam(defaultValue = "20") int size,
      @Parameter(description = "Filter by category") @RequestParam(required = false)
          Category category,
      @Parameter(description = "Start date filter (yyyy-MM-dd)")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate start,
      @Parameter(description = "End date filter (yyyy-MM-dd)")
          @RequestParam(required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate end,
      @Parameter(description = "Fields to sort by: date, title, id, amount")
          @RequestParam(defaultValue = "date")
          String sortBy,
      @Parameter(description = "Direction to sort by: ASC or DESC")
          @RequestParam(defaultValue = "DESC")
          Sort.Direction direction) {

    PageRequest pageable = PageUtils.buildPageRequest(page, size, sortBy, direction);

    return ResponseEntity.ok(
        MapperUtils.createResponse(service.listExpenses(pageable, category, start, end)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
    return ResponseEntity.ok(MapperUtils.createResponse(service.getExpenseById(id)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ExpenseResponse> updateExpense(
      @PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
    return ResponseEntity.ok(MapperUtils.createResponse(service.updateExpense(id, request)));
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
