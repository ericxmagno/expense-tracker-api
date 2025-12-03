package com.ericxmagno.expense_tracker_api.util;

import static org.junit.jupiter.api.Assertions.*;

import com.ericxmagno.expense_tracker_api.dto.ExpenseResponse;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@SpringBootTest
class MapperUtilsTest {
  @Test
  void testCreateResponseFromEntity() {
    Expenses expense = new Expenses();
    expense.setId(1L);
    expense.setTitle("Lunch");
    expense.setAmount(BigDecimal.TEN);

    ExpenseResponse response = MapperUtils.createResponse(expense);

    assertEquals(1L, response.getId());
    assertEquals("Lunch", response.getTitle());
    assertEquals(BigDecimal.TEN, response.getAmount());
  }

  @Test
  void testCreateResponseFromPage() {
    Expenses expense = new Expenses();
    expense.setId(1L);
    Page<Expenses> page = new PageImpl<>(List.of(expense));

    Page<ExpenseResponse> responsePage = MapperUtils.createResponse(page);

    assertEquals(1, responsePage.getTotalElements());
    assertEquals(1L, responsePage.getContent().getFirst().getId());
  }
}
