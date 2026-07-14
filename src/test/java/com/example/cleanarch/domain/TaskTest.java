package com.example.cleanarch.domain;

import com.example.cleanarch.domain.model.Task;
import com.example.cleanarch.domain.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pure domain unit tests - no Spring context, no database. Fast and offline.
 */
class TaskTest {

    @Test
    void createStartsInTodo() {
        Task task = Task.create("Write docs", "for the template");
        assertEquals(TaskStatus.TODO, task.status());
        assertEquals("Write docs", task.title());
    }

    @Test
    void withStatusReturnsNewImmutableInstance() {
        Task original = Task.create("Ship it", "");
        Task moved = original.withStatus(TaskStatus.DONE);

        assertNotSame(original, moved);
        assertEquals(TaskStatus.TODO, original.status(), "original must not be mutated");
        assertEquals(TaskStatus.DONE, moved.status());
        assertEquals(original.id(), moved.id(), "identity is preserved across transitions");
    }

    @Test
    void blankTitleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Task.create("  ", "desc"));
    }
}
