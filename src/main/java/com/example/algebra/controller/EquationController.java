package com.example.algebra.controller;

import com.example.algebra.dto.*;
import com.example.algebra.model.Equation;
import com.example.algebra.service.EquationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equations")
@Tag(
    name = "Algebra Equation API",
    description =
        "APIs to store, retrieve, and evaluate algebraic equations using expression trees.")
public class EquationController {

  private final EquationService equationService;

  public EquationController(EquationService equationService) {
    this.equationService = equationService;
  }

  @PostMapping("/store")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Store an algebraic equation",
      description =
          """
                    Accepts an algebraic equation, converts it into postfix notation,
                    builds an expression tree, and stores the tree in memory.
                    Returns a unique equation ID for future retrieval and evaluation.

                    Example: 3x + 2y - z
                    """)
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Equation stored successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid or empty equation")
  })
  public StoreEquationResponse storeEquation(@Valid @RequestBody StoreEquationRequest request) {

    String equationId = equationService.storeEquation(request.equation());

    return new StoreEquationResponse("Equation stored successfully", equationId);
  }

  @GetMapping
  @Operation(
      summary = "Retrieve all stored equations",
      description =
          """
                    Retrieves all equations currently stored in memory.
                    Each equation is reconstructed into readable infix notation
                    from its expression tree.
                    """)
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Equations retrieved successfully")
  })
  public List<EquationResponse> getAllEquations() {
    return equationService.getAllEquations().stream().map(this::convertToEquationResponse).toList();
  }

  @PostMapping("/{equationId}/evaluate")
  @Operation(
      summary = "Evaluate a stored equation",
      description =
          """
                    Evaluates a previously stored equation using the supplied
                    variable values. The expression tree is traversed to calculate
                    the result.

                    Example equation: 3x + 2y - z
                    Example variables: x = 2, y = 3, z = 1
                    Expected result: 11
                    """)
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Equation evaluated successfully"),
    @ApiResponse(responseCode = "400", description = "Missing variables or invalid evaluation"),
    @ApiResponse(responseCode = "404", description = "Equation ID not found")
  })
  public EvaluationResponse evaluateEquation(
      @Parameter(
              description = "Unique ID of the stored equation",
              example = "123e4567-e89b-12d3-a456-426614174000")
          @PathVariable
          String equationId,
      @Valid @RequestBody EvaluateEquationRequest request) {

    Equation equation = equationService.getEquationById(equationId);

    double result = equationService.evaluateEquation(equationId, request.variables());

    String equationText = equationService.reconstructEquation(equation.getRoot());

    return new EvaluationResponse(equationText, request.variables(), result);
  }

  private EquationResponse convertToEquationResponse(Equation equation) {
    String equationText = equationService.reconstructEquation(equation.getRoot());

    return new EquationResponse(equation.getEquationId(), equationText);
  }
}
