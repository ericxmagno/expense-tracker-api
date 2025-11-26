package com.ericxmagno.expense_tracker_api.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;

  @Mock private WebRequest webRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    handler = new GlobalExceptionHandler();
    when(webRequest.getDescription(false)).thenReturn("request description");
  }

  @Test
  void testHandleHttpMessageNotReadableException() {
    // Create the exception
    HttpMessageNotReadableException ex =
        new HttpMessageNotReadableException("Malformed JSON", null);

    // Call the handler
    ResponseEntity<ErrorDetails> response =
        handler.handleHttpMessageNotReadableException(ex, webRequest);

    // Assertions
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    ErrorDetails body = response.getBody();
    assertNotNull(body);
    assertEquals("Malformed JSON", body.getMessage());
    assertEquals("request description", body.getDetails());
    assertEquals("BAD_REQUEST", body.getErrorCode());

    // Timestamp should be set
    assertTrue(body.getTimestamp() > 0);
  }

  @Test
  void testHandleExpenseNotFoundException() {
    ExpenseNotFoundException ex = new ExpenseNotFoundException("Not found");

    ResponseEntity<ErrorDetails> response = handler.handleExpenseNotFoundException(ex, webRequest);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assert response.getBody() != null;
    assertEquals("Not found", response.getBody().getMessage());
    assertEquals("NOT_FOUND", response.getBody().getErrorCode());
  }

  @Test
  void testHandleGenericException() {
    Exception ex = new Exception("Internal Server Error");

    ResponseEntity<ErrorDetails> response = handler.handleGlobalException(ex, webRequest);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assert response.getBody() != null;
    assertEquals("Internal Server Error", response.getBody().getMessage());
    assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getErrorCode());
  }

  @Test
  void testHandleExpenseValidationException() {
    ExpenseValidationException ex =
        new ExpenseValidationException("Invalid expense", new Throwable());

    ResponseEntity<ErrorDetails> response =
        handler.handleExpenseValidationException(ex, webRequest);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assert response.getBody() != null;
    assertEquals("Invalid expense", response.getBody().getMessage());
    assertEquals("BAD_REQUEST", response.getBody().getErrorCode());
  }

  @Test
  void testHandleMethodArgumentTypeMismatch() {
    MethodArgumentTypeMismatchException ex =
        new MethodArgumentTypeMismatchException("abc", Integer.class, "id", null, null);

    ResponseEntity<Map<String, String>> response = handler.handleTypeMismatch(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assert response.getBody() != null;
    assertEquals("INVALID_PARAMETER", response.getBody().get("error"));
  }
}
