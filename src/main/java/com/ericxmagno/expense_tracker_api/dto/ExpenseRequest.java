package com.ericxmagno.expense_tracker_api.dto;

import com.ericxmagno.expense_tracker_api.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ExpenseRequest {

  @Schema(description = "Expense title", example = "Lunch")
  @NotBlank
  private String title;

  @Schema(description = "Amount spent", example = "12.5", type = "number", format = "double")
  @NotNull
  private BigDecimal amount;

  @Schema(description = "Expense category", example = "FOOD")
  @NotNull
  private Category category;

  @Schema(description = "Date of expense", example = "2025-11-27", type = "string", format = "date")
  @NotNull
  private LocalDate date;

  @Schema(description = "Optional description", example = "Lunch at Starbucks")
  private String description;
}
