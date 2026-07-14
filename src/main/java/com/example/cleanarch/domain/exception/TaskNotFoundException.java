package com.example.cleanarch.domain.exception;

import java.util.UUID;

/**
 * Raised when a task cannot be found. Pure domain exception - the presentation
 * layer maps it to an HTTP 404, but the domain itself knows nothing about HTTP.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(UUID id) {
        super("Task not found: " + id);
    }
}
