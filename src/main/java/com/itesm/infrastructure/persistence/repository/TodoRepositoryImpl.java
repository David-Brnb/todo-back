package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Todo;
import com.itesm.domain.repository.TodoRepository;
import com.itesm.infrastructure.mapper.TodoMapper;
import com.itesm.infrastructure.persistence.entity.TodoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TodoRepositoryImpl implements TodoRepository, PanacheRepositoryBase<TodoEntity, UUID> {
    @Override
    @Transactional
    public Todo save(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
        persist(entity);
        return TodoMapper.toDomain(entity);
    }

    public List<Todo> findOll(){
        List<Todo> todos = new ArrayList<>();

        for(TodoEntity entity : findAll().list()){
            todos.add(TodoMapper.toDomain(entity));
        }

        return todos;
    }

    public Todo findBiId(UUID id) {
        TodoEntity entity = findById(id);

        if(entity == null){
            return null;
        }

        return TodoMapper.toDomain(entity);
    }

    @Transactional
    public boolean deleteBID(UUID id) {
        TodoEntity entity = findById(id);
        if(entity == null){
            return false;
        }

        deleteById(id);
        return true;
    }

    @Transactional
    public Todo setTaskCompleted(UUID id) {
        TodoEntity entity = findById(id);
        if(entity == null){
            return null;
        }

        entity.setCompleted(true);
        persist(entity);
        return TodoMapper.toDomain(entity);
    }
}