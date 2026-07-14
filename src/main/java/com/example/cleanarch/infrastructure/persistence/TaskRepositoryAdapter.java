package com.example.cleanarch.infrastructure.persistence;

import com.example.cleanarch.domain.model.Task;
import com.example.cleanarch.domain.port.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter that implements the domain {@link TaskRepository} port on top of Spring
 * Data JPA. This is the seam where the pure domain meets the persistence
 * technology: it translates between {@link Task} and {@link TaskEntity}.
 */
@Repository
public class TaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskRepository jpaRepository;

    public TaskRepositoryAdapter(SpringDataTaskRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        return jpaRepository.save(TaskEntity.fromDomain(task)).toDomain();
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id).map(TaskEntity::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll().stream().map(TaskEntity::toDomain).toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
