package com.example.algebra.parser;

import com.example.algebra.model.TreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {

  private static final Pattern TOKEN_PATTERN =
      Pattern.compile("\\d+(\\.\\d+)?|[a-zA-Z]|[+\\-*/^()]");

  private int precedence(String operator) {

    return switch (operator) {
      case "+", "-" -> 1;
      case "*", "/" -> 2;
      case "u-" -> 3;
      case "^" -> 4;
      default -> -1;
    };
  }

  private boolean isOperator(String token) {
    return token.matches("[+\\-*/^]") || token.equals("u-");
  }

  private boolean isOperand(String token) {
    return token.matches("\\d+(\\.\\d+)?|[a-zA-Z]");
  }

  public List<String> tokenize(String equation) {

    if (equation == null || equation.isBlank()) {
      throw new IllegalArgumentException("Equation cannot be empty");
    }

    List<String> tokens = new ArrayList<>();

    Matcher matcher = TOKEN_PATTERN.matcher(equation);

    int lastPosition = 0;

    while (matcher.find()) {

      String skipped = equation.substring(lastPosition, matcher.start());

      if (!skipped.isBlank()) {
        throw new IllegalArgumentException("Invalid character in equation: " + skipped);
      }

      tokens.add(matcher.group());
      lastPosition = matcher.end();
    }

    if (!equation.substring(lastPosition).isBlank()) {
      throw new IllegalArgumentException("Invalid character in equation");
    }

    if (tokens.isEmpty()) {
      throw new IllegalArgumentException("Invalid equation");
    }

    List<String> result = new ArrayList<>();

    for (int i = 0; i < tokens.size(); i++) {

      String current = tokens.get(i);

      result.add(current);

      if (i < tokens.size() - 1) {

        String next = tokens.get(i + 1);

        boolean currentCanMultiply = isOperand(current) || current.equals(")");

        boolean nextCanMultiply = isOperand(next) || next.equals("(");

        if (currentCanMultiply && nextCanMultiply) {
          result.add("*");
        }
      }
    }

    for (int i = 0; i < result.size(); i++) {

      if (result.get(i).equals("-")) {

        if (i == 0 || isOperator(result.get(i - 1)) || result.get(i - 1).equals("(")) {

          result.set(i, "u-");
        }
      }
    }

    return result;
  }

  private void validateTokens(List<String> tokens) {

    if (tokens == null || tokens.isEmpty()) {
      throw new IllegalArgumentException("Equation cannot be empty");
    }

    boolean expectingOperand = true;
    int parenthesesCount = 0;

    for (String token : tokens) {

      if (isOperand(token)) {

        if (!expectingOperand) {
          throw new IllegalArgumentException("Missing operator before: " + token);
        }

        expectingOperand = false;
      } else if (token.equals("(")) {

        if (!expectingOperand) {
          throw new IllegalArgumentException("Missing operator before opening parenthesis");
        }

        parenthesesCount++;
        expectingOperand = true;
      } else if (token.equals(")")) {

        if (expectingOperand) {
          throw new IllegalArgumentException("Unexpected closing parenthesis");
        }

        parenthesesCount--;
        expectingOperand = false;

        if (parenthesesCount < 0) {
          throw new IllegalArgumentException("Mismatched parentheses");
        }
      } else if (token.equals("u-")) {

        if (!expectingOperand) {
          throw new IllegalArgumentException("Unexpected unary minus");
        }

        expectingOperand = true;
      } else if (isOperator(token)) {

        if (expectingOperand) {
          throw new IllegalArgumentException("Missing operand before operator: " + token);
        }

        expectingOperand = true;
      } else {
        throw new IllegalArgumentException("Invalid token: " + token);
      }
    }

    if (parenthesesCount != 0) {
      throw new IllegalArgumentException("Mismatched parentheses");
    }

    if (expectingOperand) {
      throw new IllegalArgumentException("Expression cannot end with an operator");
    }
  }

  public List<String> toPostfix(List<String> tokens) {

    validateTokens(tokens);

    List<String> output = new ArrayList<>();
    Stack<String> operators = new Stack<>();

    for (String token : tokens) {

      if (isOperand(token)) {
        output.add(token);
      } else if (token.equals("(")) {
        operators.push(token);
      } else if (token.equals(")")) {

        while (!operators.isEmpty() && !operators.peek().equals("(")) {

          output.add(operators.pop());
        }

        if (operators.isEmpty()) {
          throw new IllegalArgumentException("Mismatched parentheses");
        }

        operators.pop();
      } else if (token.equals("u-")) {
        operators.push(token);
      } else if (isOperator(token)) {

        while (!operators.isEmpty()
            && isOperator(operators.peek())
            && (precedence(operators.peek()) > precedence(token)
                || (precedence(operators.peek()) == precedence(token) && !token.equals("^")))) {

          output.add(operators.pop());
        }

        operators.push(token);
      } else {
        throw new IllegalArgumentException("Invalid token: " + token);
      }
    }

    while (!operators.isEmpty()) {

      String operator = operators.pop();

      if (operator.equals("(")) {
        throw new IllegalArgumentException("Mismatched parentheses");
      }

      output.add(operator);
    }

    return output;
  }

  public TreeNode buildExpressionTree(List<String> postfix) {

    Stack<TreeNode> stack = new Stack<>();

    for (String token : postfix) {

      if (token.equals("u-")) {

        if (stack.isEmpty()) {
          throw new IllegalArgumentException("Invalid expression");
        }

        TreeNode operand = stack.pop();

        stack.push(new TreeNode("u-", operand, null));
      } else if (isOperator(token)) {

        if (stack.size() < 2) {
          throw new IllegalArgumentException("Invalid expression");
        }

        TreeNode right = stack.pop();
        TreeNode left = stack.pop();

        stack.push(new TreeNode(token, left, right));
      } else {
        stack.push(new TreeNode(token));
      }
    }

    if (stack.size() != 1) {
      throw new IllegalArgumentException("Invalid expression");
    }

    return stack.pop();
  }

  public double evaluate(TreeNode node, Map<String, Double> variables) {

    if (node == null) {
      throw new IllegalArgumentException("Invalid expression tree");
    }

    String value = node.getValue();

    if (value.equals("u-")) {
      return -evaluate(node.getLeft(), variables);
    }

    if (isOperator(value)) {

      double left = evaluate(node.getLeft(), variables);
      double right = evaluate(node.getRight(), variables);

      return switch (value) {
        case "+" -> left + right;

        case "-" -> left - right;

        case "*" -> left * right;

        case "/" -> {
          if (right == 0) {
            throw new ArithmeticException("Division by zero");
          }
          yield left / right;
        }

        case "^" -> Math.pow(left, right);

        default -> throw new IllegalArgumentException("Unknown operator: " + value);
      };
    }

    try {
      return Double.parseDouble(value);

    } catch (NumberFormatException exception) {

      if (variables == null) {
        throw new IllegalArgumentException("Variable values are required");
      }

      Double variableValue = variables.get(value);

      if (variableValue == null) {
        throw new IllegalArgumentException("Missing value for variable: " + value);
      }

      return variableValue;
    }
  }
}
