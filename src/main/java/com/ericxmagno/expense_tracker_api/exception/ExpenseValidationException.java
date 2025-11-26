package com.ericxmagno.expense_tracker_api.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class ExpenseValidationException extends DataIntegrityViolationException {

  public ExpenseValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
