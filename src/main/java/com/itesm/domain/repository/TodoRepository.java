package com.itesm.domain.repository;

import com.itesm.domain.models.Todo;

import java.util.List;
import java.util.UUID;

public interface TodoRepository {
    Todo save(Todo todo);

    List<Todo> findAllTodos();

    Todo findBiId(UUID id);

    boolean deleteBID(UUID id);

    Todo setTaskCompleted(UUID id);

    /**
     * Encuentra todos los Todos con eager loading de owner, categories y comments
     * usando JOIN FETCH para evitar N+1 queries
     */
    List<Todo> findAllWithJoinFetch();

    /**
     * Encuentra un Todo específico con eager loading de owner, categories y comments
     */
    Todo findByIdWithJoinFetch(UUID id);
}