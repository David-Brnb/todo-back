package com.itesm.application.usecase.todos;

import com.itesm.application.dto.CreateTodoDTO;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class CreateTodoUseCase {
    private final TodoRepository todoRepository;
    private final AuthenticatedUserContext authenticatedUserContext;

    @Inject
    public CreateTodoUseCase(TodoRepository todoRepository,  AuthenticatedUserContext authenticatedUserContext) {
        this.todoRepository = todoRepository;
        this.authenticatedUserContext = authenticatedUserContext;
    }

    public Todo execute(CreateTodoDTO createTodoDTO) {
        Todo todo = new Todo();
        todo.setUuid(UUID.randomUUID());
        todo.setCreatedAt(LocalDateTime.now());
        todo.setTitle(createTodoDTO.getTitle());
        todo.setDescription(createTodoDTO.getDescription());
        todo.setCompleted(false);
        todo.setOwnerId(authenticatedUserContext.getCurrentUser().getUserId());
        return todoRepository.save(todo);
    }
}