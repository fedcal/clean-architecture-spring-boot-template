package com.example.cleanarch.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * HTTP request body for creating a task. Validated at the boundary before it is
 * mapped to the application-layer command.
 */
public record CreateTaskRequest(
        @NotBlank @Size(max = 200) String title,
        @Size(max = 2000) String description) {
}
