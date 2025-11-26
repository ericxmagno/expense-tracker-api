package com.ericxmagno.expense_tracker_api.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDetails> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex, WebRequest request) {
    ErrorDetails errorDetails =
        new ErrorDetails(
            System.currentTimeMillis(),
            ex.getMessage(),
            request.getDescription(false),
            "BAD_REQUEST");
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ExpenseValidationException.class)
  public ResponseEntity<ErrorDetails> handleExpenseValidationException(
      ExpenseValidationException ex, WebRequest request) {
    ErrorDetails errorDetails =
        new ErrorDetails(
            System.currentTimeMillis(),
            ex.getMessage(),
            request.getDescription(false),
            "BAD_REQUEST");
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ExpenseNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleExpenseNotFoundException(
      ExpenseNotFoundException ex, WebRequest request) {
    ErrorDetails errorDetails =
        new ErrorDetails(
            System.currentTimeMillis(),
            ex.getMessage(),
            request.getDescription(false),
            "NOT_FOUND");
    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
  }

  // Handles generic exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest request) {
    ErrorDetails errorDetails =
        new ErrorDetails(
            System.currentTimeMillis(),
            ex.getMessage(),
            request.getDescription(false),
            "INTERNAL_SERVER_ERROR");
    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    String param = ex.getName();
    String value = ex.getValue() != null ? ex.getValue().toString() : "null";
    String message = String.format("Request Parameter %s '%s' is invalid.", param, value);

    return ResponseEntity.badRequest()
        .body(Map.of("error", "INVALID_PARAMETER", "message", message));
  }
}
