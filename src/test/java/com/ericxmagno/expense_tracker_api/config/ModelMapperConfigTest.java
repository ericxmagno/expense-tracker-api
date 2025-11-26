package com.ericxmagno.expense_tracker_api.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ModelMapperConfigTest {

  @Autowired private ModelMapper modelMapper;

  @Test
  void contextLoadsAndBeanIsAvailable() {
    assertNotNull(modelMapper);
  }
}
