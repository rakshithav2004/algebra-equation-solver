package com.example.algebra.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Details of a stored algebraic equation")
public record EquationResponse(
    @Schema(
            description = "Unique identifier of the equation",
            example = "123e4567-e89b-12d3-a456-426614174000")
        String equationId,
    @Schema(
            description = "Reconstructed equation in infix notation",
            example = "((3 * x) + (2 * y)) - z")
        String equation) {}
