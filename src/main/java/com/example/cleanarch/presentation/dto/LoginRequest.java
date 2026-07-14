package com.example.cleanarch.presentation.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * HTTP request body for the demo login endpoint. This is a presentation-layer
 * DTO - the HTTP shape is decoupled from the application command shape.
 */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
