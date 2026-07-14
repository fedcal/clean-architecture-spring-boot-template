package com.example.cleanarch.domain.model;

/**
 * Lifecycle states of a {@link Task}. Pure domain enum - no framework imports.
 */
public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}
