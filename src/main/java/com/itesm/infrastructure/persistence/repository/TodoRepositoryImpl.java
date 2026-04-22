package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import com.itesm.infrastructure.mapper.TodoMapper;
import com.itesm.infrastructure.persistence.entity.TodoEntity;
import com.itesm.infrastructure.persistence.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TodoRepositoryImpl implements TodoRepository, PanacheRepositoryBase<TodoEntity, UUID> {

    @Inject
    EntityManager em;

    @Override
    @Transactional
    public Todo save(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
        if (todo.getOwnerId() != null) {
            entity.setOwner(getEntityManager().getReference(UserEntity.class, todo.getOwnerId())); // que es esto?
        }

        persist(entity);
        return TodoMapper.toDomain(entity);
    }

    public List<Todo> findAllTodos() {
        List<TodoEntity> todoEntities = findAll().stream().toList();

        List<Todo> todos = new ArrayList<>();

        for (TodoEntity todoEntity : todoEntities) {
            todos.add(TodoMapper.toDomain(todoEntity));
        }

        return todos;
    }

    public Todo findBiId(UUID id) {
        TodoEntity entity = findById(id);

        if (entity == null) {
            return null;
        }

        return TodoMapper.toDomain(entity);
    }

    @Transactional
    public boolean deleteBID(UUID id) {
        TodoEntity entity = findById(id);
        if (entity == null) {
            return false;
        }

        deleteById(id);
        return true;
    }

    @Transactional
    public Todo setTaskCompleted(UUID id) {
        TodoEntity entity = findById(id);
        if (entity == null) {
            return null;
        }

        entity.setCompleted(true);
        persist(entity);
        return TodoMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public List<Todo> findAllWithJoinFetch() {
        // Query 1: Cargar todos con owner y categories (LEFT JOIN FETCH)
        List<TodoEntity> todos = em.createQuery(
                "SELECT DISTINCT t FROM TodoEntity t " +
                        "LEFT JOIN FETCH t.owner " +
                        "LEFT JOIN FETCH t.categories",
                TodoEntity.class
        ).getResultList();

        // Query 2: Si hay resultados, cargar comments y authors
        if (!todos.isEmpty()) {
            em.createQuery(
                            "SELECT DISTINCT t FROM TodoEntity t " +
                                    "LEFT JOIN FETCH t.comments c " +
                                    "LEFT JOIN FETCH c.author " +
                                    "WHERE t IN :todos",
                            TodoEntity.class
                    ).setParameter("todos", todos)
                    .getResultList();
        }

        return todos.stream()
                .map(TodoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Todo findByIdWithJoinFetch(UUID id) {
        // Query 1: Cargar todo con owner y categories
        TodoEntity todo = em.createQuery(
                        "SELECT DISTINCT t FROM TodoEntity t " +
                                "LEFT JOIN FETCH t.owner " +
                                "LEFT JOIN FETCH t.categories " +
                                "WHERE t.id = :id",
                        TodoEntity.class
                ).setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (todo == null) {
            return null;
        }

        // Query 2: Cargar comments y authors
        em.createQuery(
                        "SELECT DISTINCT t FROM TodoEntity t " +
                                "LEFT JOIN FETCH t.comments c " +
                                "LEFT JOIN FETCH c.author " +
                                "WHERE t.id = :id",
                        TodoEntity.class
                ).setParameter("id", id)
                .getResultList();

        return TodoMapper.toDomain(todo);
    }
}
