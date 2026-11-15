package com.example.algebra.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.algebra.exception.EquationNotFoundException;
import com.example.algebra.model.Equation;
import com.example.algebra.repository.EquationRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquationServiceTest {

  private EquationService equationService;

  @BeforeEach
  void setUp() {
    EquationRepository repository = new EquationRepository();
    equationService = new EquationService(repository);
  }

  @Test
  void shouldStoreEquationSuccessfully() {
    String equationId = equationService.storeEquation("3x + 2y - z");

    assertNotNull(equationId);
    assertFalse(equationId.isBlank());
  }

  @Test
  void shouldRetrieveStoredEquation() {
    String equationId = equationService.storeEquation("3x + 2y - z");

    Equation equation = equationService.getEquationById(equationId);

    assertNotNull(equation);
    assertEquals(equationId, equation.getEquationId());
  }

  @Test
  void shouldRetrieveAllStoredEquations() {
    equationService.storeEquation("2x + 3");
    equationService.storeEquation("5y - 1");

    assertEquals(2, equationService.getAllEquations().size());
  }

  @Test
  void shouldEvaluateEquationCorrectly() {
    String equationId = equationService.storeEquation("3x + 2y - z");

    double result =
        equationService.evaluateEquation(equationId, Map.of("x", 2.0, "y", 3.0, "z", 1.0));

    assertEquals(11.0, result, 0.0001);
  }

  @Test
  void shouldReconstructStoredEquation() {
    String equationId = equationService.storeEquation("3x + 2y - z");

    Equation equation = equationService.getEquationById(equationId);

    String reconstructed = equationService.reconstructEquation(equation.getRoot());

    assertEquals("(((3 * x) + (2 * y)) - z)", reconstructed);
  }

  @Test
  void shouldThrowExceptionForUnknownEquationId() {
    assertThrows(
        EquationNotFoundException.class, () -> equationService.getEquationById("unknown-id"));
  }

  @Test
  void shouldRejectMissingVariableDuringEvaluation() {
    String equationId = equationService.storeEquation("3x + 2");

    assertThrows(
        IllegalArgumentException.class,
        () -> equationService.evaluateEquation(equationId, Map.of()));
  }
}
