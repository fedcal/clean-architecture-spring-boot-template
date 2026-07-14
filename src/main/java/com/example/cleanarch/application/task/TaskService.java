package com.example.cleanarch.application.task;

import com.example.cleanarch.application.task.dto.CreateTaskCommand;
import com.example.cleanarch.application.task.dto.TaskView;
import com.example.cleanarch.domain.exception.TaskNotFoundException;
import com.example.cleanarch.domain.model.Task;
import com.example.cleanarch.domain.model.TaskStatus;
import com.example.cleanarch.domain.port.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the {@link TaskUseCase} port.
 *
 * <p>It orchestrates the domain and the repository port, and it depends ONLY on
 * domain types ({@link Task}, {@link TaskRepository}) and application DTOs -
 * never on any infrastructure class. That constraint is enforced at build time
 * by {@code CleanArchitectureTest}.
 */
@Service
public class TaskService implements TaskUseCase {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public TaskView create(CreateTaskCommand command) {
        Task task = Task.create(command.title(), command.description());
        return TaskView.from(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskView changeStatus(UUID id, TaskStatus status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return TaskView.from(taskRepository.save(task.withStatus(status)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskView> list() {
        return taskRepository.findAll().stream().map(TaskView::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskView getById(UUID id) {
        return taskRepository.findById(id)
                .map(TaskView::from)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (taskRepository.findById(id).isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
