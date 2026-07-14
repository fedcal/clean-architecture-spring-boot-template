package com.example.cleanarch.application.task.dto;

/**
 * Input command for creating a task. Application-layer DTO - decoupled from both
 * the HTTP request shape (presentation) and the JPA entity shape (infrastructure).
 */
public record CreateTaskCommand(String title, String description) {
}
