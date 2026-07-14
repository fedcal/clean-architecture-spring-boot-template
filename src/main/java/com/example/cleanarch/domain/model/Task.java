package com.example.cleanarch.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * The example aggregate root for this template: a generic Task.
 *
 * <p>This is a pure domain object - it has no Spring, no JPA, and no other
 * framework dependencies (enforced by {@code CleanArchitectureTest}). It is
 * immutable: state transitions return a new instance rather than mutating in
 * place, which keeps the aggregate side-effect free and easy to reason about.
 *
 * <p>Replace this with your own aggregate; it exists only to make the four
 * layers wire up end to end.
 */
public final class Task {

    private final UUID id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final Instant createdAt;

    public Task(UUID id, String title, String description, TaskStatus status, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = requireNonBlank(title, "title");
        this.description = description == null ? "" : description;
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    /** Factory for a brand-new task: always starts in {@link TaskStatus#TODO}. */
    public static Task create(String title, String description) {
        return new Task(UUID.randomUUID(), title, description, TaskStatus.TODO, Instant.now());
    }

    /** Returns a new Task with the given status - never mutates this instance. */
    public Task withStatus(TaskStatus newStatus) {
        return new Task(id, title, description, newStatus, createdAt);
    }

    private static String requireNonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    public UUID id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public TaskStatus status() {
        return status;
    }

    public Instant createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Task task)) {
            return false;
        }
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
