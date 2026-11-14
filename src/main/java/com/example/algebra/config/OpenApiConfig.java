package com.example.algebra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI algebraEquationOpenAPI() {

    return new OpenAPI()
        .info(
            new Info()
                .title("Algebra Equation Solver API")
                .description(
                    """
                                REST API for storing, retrieving, and evaluating
                                algebraic equations using expression trees.

                                Features:
                                - Store algebraic equations in memory.
                                - Convert expressions into postfix notation.
                                - Build expression trees for stored equations.
                                - Retrieve equations in readable infix notation.
                                - Evaluate equations using variable values.
                                - Validate expressions and handle evaluation errors.

                                Example equation: 3x + 2y - z
                                """)
                .version("1.0.0")
                .contact(new Contact().name("API Support").email("support@example.com"))
                .license(new License().name("MIT License")));
  }
}
