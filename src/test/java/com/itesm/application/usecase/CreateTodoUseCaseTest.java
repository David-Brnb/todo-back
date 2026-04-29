package com.itesm.application.usecase;

import com.itesm.application.dto.CreateTodoDTO;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.application.usecase.todos.CreateTodoUseCase;
import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class CreateTodoUseCaseTest {
    private TodoRepository todoRepository;
    private AuthenticatedUserContext authenticatedUserContext;
    private CreateTodoUseCase createTodoUseCase;

    @BeforeEach
    void setUp() {
        todoRepository = Mockito.mock(TodoRepository.class);
        authenticatedUserContext = Mockito.mock(AuthenticatedUserContext.class);

        CurrentUser currentUser = new CurrentUser(
                "firebase-123", "test@test.com", "USER", "Test User", UUID.randomUUID()
        );

        when(authenticatedUserContext.getCurrentUser()).thenReturn(currentUser);
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        createTodoUseCase = new CreateTodoUseCase(todoRepository, authenticatedUserContext);
    }

    @Test
    void execute_shouldCreateTodoWithCorrectFields() {
        CreateTodoDTO dto = new CreateTodoDTO("Mi tarea", "Descripcion de la tarea");
        Todo result = createTodoUseCase.execute(dto);
        assertNotNull(result);
        assertEquals("Mi tarea", result.getTitle());
        assertEquals("Descripcion de la tarea", result.getDescription());
        assertFalse(result.isCompleted());
        assertNotNull(result.getUuid());
        assertNotNull(result.getCreatedAt());
    }

}
