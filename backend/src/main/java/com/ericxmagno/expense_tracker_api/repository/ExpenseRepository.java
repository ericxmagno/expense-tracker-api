package com.ericxmagno.expense_tracker_api.repository;

import com.ericxmagno.expense_tracker_api.model.Category;
import com.ericxmagno.expense_tracker_api.model.Expenses;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expenses, Long> {

  Page<Expenses> findByCategory(Category category, Pageable page);

  Page<Expenses> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);

  List<Expenses> findByDateBetween(LocalDate start, LocalDate end);
}
