package com.example.algebra.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DtoValidationTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  static void tearDown() {
    validatorFactory.close();
  }

  @Test
  void shouldRejectBlankEquation() {
    var request = new StoreEquationRequest(" ");

    assertFalse(validator.validate(request).isEmpty());
  }

  @Test
  void shouldRejectNullEquation() {
    var request = new StoreEquationRequest(null);

    assertFalse(validator.validate(request).isEmpty());
  }

  @Test
  void shouldAcceptValidEquation() {
    var request = new StoreEquationRequest("3x + 2y - z");

    assertTrue(validator.validate(request).isEmpty());
  }

  @Test
  void shouldRejectNullVariables() {
    var request = new EvaluateEquationRequest(null);

    assertFalse(validator.validate(request).isEmpty());
  }

  @Test
  void shouldAcceptValidVariables() {
    var request = new EvaluateEquationRequest(Map.of("x", 2.0, "y", 3.0));

    assertTrue(validator.validate(request).isEmpty());
  }
}
