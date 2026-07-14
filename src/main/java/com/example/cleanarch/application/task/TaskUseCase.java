package com.example.cleanarch.application.task;

import com.example.cleanarch.application.task.dto.CreateTaskCommand;
import com.example.cleanarch.application.task.dto.TaskView;
import com.example.cleanarch.domain.model.TaskStatus;

import java.util.List;
import java.util.UUID;

/**
 * Inbound ("driving") port for the Task feature. The presentation layer depends
 * on this interface only; it never sees the implementation nor any
 * infrastructure type.
 */
public interface TaskUseCase {

    TaskView create(CreateTaskCommand command);

    TaskView changeStatus(UUID id, TaskStatus status);

    List<TaskView> list();

    TaskView getById(UUID id);

    void delete(UUID id);
}
