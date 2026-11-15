package com.example.algebra.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.example.algebra.model.TreeNode;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpressionParserTest {

  private ExpressionParser parser;

  @BeforeEach
  void setUp() {
    parser = new ExpressionParser();
  }

  @Test
  void shouldEvaluateSimpleArithmeticExpression() {
    var tokens = parser.tokenize("2 + 3 * 4");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    double result = parser.evaluate(root, Map.of());

    assertEquals(14.0, result, 0.0001);
  }

  @Test
  void shouldEvaluateEquationWithVariables() {
    var tokens = parser.tokenize("3x + 2y - z");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    double result = parser.evaluate(root, Map.of("x", 2.0, "y", 3.0, "z", 1.0));

    assertEquals(11.0, result, 0.0001);
  }

  @Test
  void shouldHandleParentheses() {
    var tokens = parser.tokenize("(2 + 3) * 4");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    double result = parser.evaluate(root, Map.of());

    assertEquals(20.0, result, 0.0001);
  }

  @Test
  void shouldHandleUnaryMinus() {
    var tokens = parser.tokenize("-3 + 5");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    double result = parser.evaluate(root, Map.of());

    assertEquals(2.0, result, 0.0001);
  }

  @Test
  void shouldHandleExponentiation() {
    var tokens = parser.tokenize("2 ^ 3 ^ 2");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    double result = parser.evaluate(root, Map.of());

    assertEquals(512.0, result, 0.0001);
  }

  @Test
  void shouldRejectInvalidExpression() {
    assertThrows(IllegalArgumentException.class, () -> parser.toPostfix(parser.tokenize("3 +")));
  }

  @Test
  void shouldRejectMissingVariable() {
    var tokens = parser.tokenize("3x + 2");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    assertThrows(IllegalArgumentException.class, () -> parser.evaluate(root, Map.of()));
  }

  @Test
  void shouldRejectDivisionByZero() {
    var tokens = parser.tokenize("10 / 0");
    var postfix = parser.toPostfix(tokens);
    TreeNode root = parser.buildExpressionTree(postfix);

    assertThrows(ArithmeticException.class, () -> parser.evaluate(root, Map.of()));
  }
}
