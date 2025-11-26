package com.ericxmagno.expense_tracker_api.util;

import com.ericxmagno.expense_tracker_api.sort.SortableFields;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtils {

  public static PageRequest buildPageRequest(
      int page, int size, String sortBy, Sort.Direction direction) {
    SortableFields sortField = SortableFields.fromString(sortBy);
    Sort sort = Sort.by(direction, sortField.field());
    return PageRequest.of(page, size, sort);
  }
}
