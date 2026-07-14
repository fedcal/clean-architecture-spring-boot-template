package com.example.cleanarch.infrastructure.persistence;

import com.example.cleanarch.domain.model.Task;
import com.example.cleanarch.domain.model.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA persistence model for {@link Task}. This lives in the infrastructure layer
 * and is intentionally separate from the domain aggregate so that persistence
 * annotations never leak into the pure domain object.
 */
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TaskStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected TaskEntity() {
        // required by JPA
    }

    private TaskEntity(UUID id, String title, String description, TaskStatus status, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    static TaskEntity fromDomain(Task task) {
        return new TaskEntity(task.id(), task.title(), task.description(), task.status(), task.createdAt());
    }

    Task toDomain() {
        return new Task(id, title, description, status, createdAt);
    }
}
