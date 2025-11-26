package com.ericxmagno.expense_tracker_api.model;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public enum Category {
  ENT("ENT", "Entertainment"),
  FOOD("FOOD", "Food"),
  OTHER("OTHER", "Other"),
  RENT("RENT", "Rent"),
  TRAVEL("TRAVEL", "Travel");

  private final String code;

  @Column(name = "display_name")
  private final String displayName;

  Category(String code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  public static Category getByCode(String code) {
    for (Category category : values()) {
      if (category.getCode().equalsIgnoreCase(code)) return category;
    }
    throw new IllegalArgumentException("Invalid code: " + code);
  }
}
