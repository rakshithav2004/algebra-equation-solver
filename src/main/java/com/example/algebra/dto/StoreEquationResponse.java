package com.example.algebra.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response returned after successfully storing an equation")
public record StoreEquationResponse(
    @Schema(example = "Equation stored successfully") String message,
    @Schema(
            description = "Unique identifier assigned to the stored equation",
            example = "123e4567-e89b-12d3-a456-426614174000")
        String equationId) {}
