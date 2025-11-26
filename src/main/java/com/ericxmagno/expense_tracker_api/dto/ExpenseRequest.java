package com.ericxmagno.expense_tracker_api.dto;

import com.ericxmagno.expense_tracker_api.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ExpenseRequest {

  @NotBlank private String title;

  @NotNull private BigDecimal amount;

  @NotNull private Category category;

  @NotNull private LocalDate date;

  private String description;
}
