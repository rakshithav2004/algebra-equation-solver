package com.example.algebra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for storing an algebraic equation")
public record StoreEquationRequest(
    @Schema(
            description =
                "Algebraic equation to store. Supports variables, arithmetic operators, and parentheses.",
            example = "3x + 2y - z",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Equation cannot be empty")
        String equation) {}
