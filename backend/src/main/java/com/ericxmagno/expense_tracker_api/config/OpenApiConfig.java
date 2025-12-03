package com.ericxmagno.expense_tracker_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Expense Tracker API")
                .description("REST API for managing expenses, categories, and summaries")
                .version("1.0.0")
                .contact(new Contact().name("Eric Magno").email("ericvincentmagno@gmail.com")));
  }
}
