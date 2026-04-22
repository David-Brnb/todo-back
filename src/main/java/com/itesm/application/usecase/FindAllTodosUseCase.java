package com.itesm.application.usecase;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FindAllTodosUseCase {
    private TodoRepository todoRepository;

    @Inject
    public FindAllTodosUseCase(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Obtiene todos los Todos con eager loading de owner, categories y comments.
     * Usa JOIN FETCH para evitar N+1 queries.
     * 
     * @return Lista de Todos con todas sus relaciones cargadas
     */
    public List<Todo> findAll() {
        return todoRepository.findAllWithJoinFetch();
    }
}

