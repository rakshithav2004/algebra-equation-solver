package com.example.algebra.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  void shouldHandleIllegalArgumentException() {
    ResponseEntity<Map<String, String>> response =
        handler.handleIllegalArgument(new IllegalArgumentException("Invalid equation"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid equation", response.getBody().get("error"));
  }

  @Test
  void shouldHandleArithmeticException() {
    ResponseEntity<Map<String, String>> response =
        handler.handleArithmetic(new ArithmeticException("Division by zero"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Division by zero", response.getBody().get("error"));
  }

  @Test
  void shouldHandleEquationNotFoundException() {
    ResponseEntity<Map<String, String>> response =
        handler.handleNotFound(new EquationNotFoundException("Equation not found"));

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Equation not found", response.getBody().get("error"));
  }

  @Test
  void shouldHandleGeneralException() {
    ResponseEntity<Map<String, String>> response =
        handler.handleGeneral(new Exception("Unexpected error"));

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("An unexpected error occurred", response.getBody().get("error"));
  }
}
