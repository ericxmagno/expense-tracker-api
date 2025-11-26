package com.ericxmagno.expense_tracker_api.sort;


public enum SortableFields {
  DATE("date"),
  TITLE("title"),
  ID("id"),
  AMOUNT("amount");

  private final String fieldName;

  SortableFields(String fieldName) {
    this.fieldName = fieldName;
  }

  // Case-insensitive lookup
  public static SortableFields fromString(String value) {
    for (SortableFields sf : SortableFields.values()) {
      if (sf.fieldName.equalsIgnoreCase(value) || sf.name().equalsIgnoreCase(value)) {
        return sf;
      }
    }
    throw new IllegalArgumentException("Invalid sort field: " + value);
  }

  public String field() {
    return fieldName;
  }
}
