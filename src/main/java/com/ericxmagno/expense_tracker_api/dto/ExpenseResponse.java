package com.ericxmagno.expense_tracker_api.dto;

import com.ericxmagno.expense_tracker_api.model.Category;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpenseResponse {

  private Long id;
  private String title;
  private BigDecimal amount;
  private Category category;
  private LocalDate date;
  private String description;
}
