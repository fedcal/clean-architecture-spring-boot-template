package com.example.cleanarch.application.task.dto;

import com.example.cleanarch.domain.model.Task;

import java.time.Instant;
import java.util.UUID;

/**
 * Read model returned by the use cases. Maps a domain {@link Task} to a flat,
 * serialization-friendly shape without exposing the aggregate itself.
 */
public record TaskView(UUID id, String title, String description, String status, Instant createdAt) {

    public static TaskView from(Task task) {
        return new TaskView(
                task.id(),
                task.title(),
                task.description(),
                task.status().name(),
                task.createdAt());
    }
}
