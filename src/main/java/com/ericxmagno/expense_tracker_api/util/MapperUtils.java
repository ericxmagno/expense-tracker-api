package com.ericxmagno.expense_tracker_api.util;

import com.ericxmagno.expense_tracker_api.dto.ExpenseResponse;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import org.springframework.data.domain.Page;

public final class MapperUtils {

  private MapperUtils() {
    // prevent instantiation
  }

  public static ExpenseResponse createResponse(Expenses expense) {
    return ExpenseResponse.builder()
        .id(expense.getId())
        .amount(expense.getAmount())
        .date(expense.getDate())
        .title(expense.getTitle())
        .category(expense.getCategory())
        .description(expense.getDescription())
        .build();
  }

  public static Page<ExpenseResponse> createResponse(Page<Expenses> expense) {
    return expense.map(MapperUtils::createResponse);
  }
}
