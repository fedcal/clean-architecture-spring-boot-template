package com.example.cleanarch.presentation;

import com.example.cleanarch.application.task.TaskUseCase;
import com.example.cleanarch.application.task.dto.CreateTaskCommand;
import com.example.cleanarch.application.task.dto.TaskView;
import com.example.cleanarch.domain.model.TaskStatus;
import com.example.cleanarch.presentation.dto.CreateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for the Task feature. It depends ONLY on the
 * {@link TaskUseCase} application port - never on any infrastructure class. That
 * dependency direction is enforced at build time by
 * {@code CleanArchitectureTest}.
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskUseCase taskUseCase;

    public TaskController(TaskUseCase taskUseCase) {
        this.taskUseCase = taskUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskView create(@Valid @RequestBody CreateTaskRequest request) {
        return taskUseCase.create(new CreateTaskCommand(request.title(), request.description()));
    }

    @GetMapping
    public List<TaskView> list() {
        return taskUseCase.list();
    }

    @GetMapping("/{id}")
    public TaskView getById(@PathVariable UUID id) {
        return taskUseCase.getById(id);
    }

    @PutMapping("/{id}/status")
    public TaskView changeStatus(@PathVariable UUID id, @RequestParam TaskStatus status) {
        return taskUseCase.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
