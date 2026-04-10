package com.itesm.application.usecase;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class CompletedTodoUseCase {
    private final TodoRepository todoRepository;

    @Inject
    public CompletedTodoUseCase(TodoRepository todoRepository) { this.todoRepository = todoRepository; }

    public Todo setTaskCompleted(String id) {
        UUID uuid = UUID.fromString(id);
        return todoRepository.setTaskCompleted(uuid);
    }
}
