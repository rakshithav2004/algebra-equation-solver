package com.example.algebra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Result of evaluating a stored algebraic equation")
public record EvaluationResponse(
    @Schema(description = "Reconstructed algebraic equation", example = "((3 * x) + (2 * y)) - z")
        String equation,
    @Schema(
            description = "Variable values used during evaluation",
            example = "{\"x\": 2, \"y\": 3, \"z\": 1}")
        Map<String, Double> variables,
    @Schema(description = "Calculated result of the equation", example = "11.0") double result) {}
