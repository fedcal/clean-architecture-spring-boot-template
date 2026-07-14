package com.example.cleanarch.domain.port;

import com.example.cleanarch.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port (hexagonal "driven" port) for the Task aggregate.
 *
 * <p>It lives in the domain layer and is expressed purely in terms of domain
 * types. The concrete adapter ({@code TaskRepositoryAdapter} over Spring Data
 * JPA) lives in the infrastructure layer and depends on this interface, never
 * the other way around - that is the dependency inversion that keeps the domain
 * free of persistence concerns.
 */
public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    void deleteById(UUID id);
}
