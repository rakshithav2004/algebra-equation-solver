package com.example.algebra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "Request payload containing variable values for equation evaluation")
public record EvaluateEquationRequest(
    @Schema(
            description = "Variable names and their numeric values used to evaluate the equation",
            example = "{\"x\": 2, \"y\": 3, \"z\": 1}",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Variables cannot be null")
        Map<String, Double> variables) {}
