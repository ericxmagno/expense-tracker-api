package com.ericxmagno.expense_tracker_api.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummaryResponse {

  private String month;
  private BigDecimal totalSpent;
  private Long count;
}
