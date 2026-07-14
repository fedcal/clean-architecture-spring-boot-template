package com.example.cleanarch.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository over {@link TaskEntity}. Infrastructure-only detail;
 * the domain never sees this interface (it sees the {@code TaskRepository} port).
 */
interface SpringDataTaskRepository extends JpaRepository<TaskEntity, UUID> {
}
