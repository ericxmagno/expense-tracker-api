package com.ericxmagno.expense_tracker_api.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetails {
  private long timestamp;
  private String message;
  private String details;
  private String errorCode;

  public ErrorDetails(long timestamp, String message, String details, String errorCode) {
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
    this.errorCode = errorCode;
  }

  // Getters for timestamp, message, details, errorCode
  public long getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
