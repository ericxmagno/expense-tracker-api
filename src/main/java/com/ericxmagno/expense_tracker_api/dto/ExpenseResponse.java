package com.ericxmagno.expense_tracker_api.dto;

import com.ericxmagno.expense_tracker_api.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response object for expense")
public class ExpenseResponse {

  @Schema(description = "Expense ID", example = "1")
  private Long id;

  @Schema(description = "Expense title", example = "Lunch")
  private String title;

  @Schema(description = "Expense category", example = "FOOD")
  private Category category;

  @Schema(description = "Amount spent", example = "12.5", type = "number", format = "double")
  private BigDecimal amount;

  @Schema(description = "Date of expense", example = "2025-11-27", type = "string", format = "date")
  private LocalDate date;

  @Schema(description = "Optional description", example = "Lunch at Starbucks")
  private String description;
}
