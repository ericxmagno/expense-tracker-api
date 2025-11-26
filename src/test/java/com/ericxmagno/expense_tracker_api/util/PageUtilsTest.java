package com.ericxmagno.expense_tracker_api.util;

import static org.junit.jupiter.api.Assertions.*;

import com.ericxmagno.expense_tracker_api.sort.SortableFields;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
class PageUtilsTest {

  @Test
  void buildPageRequest_ShouldCreatePageRequest_WithCorrectValues() {
    // Arrange
    int page = 2;
    int size = 10;
    String sortBy = "date";
    Sort.Direction direction = Sort.Direction.DESC;

    // Act
    PageRequest pageRequest = PageUtils.buildPageRequest(page, size, sortBy, direction);

    // Assert
    assertEquals(page, pageRequest.getPageNumber());
    assertEquals(size, pageRequest.getPageSize());

    Sort sort = pageRequest.getSort();
    assertNotNull(sort);
    assertEquals(SortableFields.DATE.field(), sort.iterator().next().getProperty());
    assertEquals(direction, sort.iterator().next().getDirection());
  }

  @Test
  void buildPageRequest_ShouldThrowException_ForInvalidSortField() {
    // Arrange
    int page = 0;
    int size = 5;
    String invalidSortBy = "invalidField";
    Sort.Direction direction = Sort.Direction.ASC;

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> PageUtils.buildPageRequest(page, size, invalidSortBy, direction));
  }
}
