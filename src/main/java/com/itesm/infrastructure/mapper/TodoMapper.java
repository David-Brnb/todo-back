package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Todo;
import com.itesm.infrastructure.persistence.entity.TodoEntity;
import java.util.stream.Collectors;

public class TodoMapper {
    public static TodoEntity toEntity(Todo todo){
        TodoEntity entity = new TodoEntity();
        entity.setId(todo.getUuid());
        entity.setTitle(todo.getTitle());
        entity.setDescription(todo.getDescription());
        entity.setCompleted(todo.isCompleted());
        entity.setCreatedAt(todo.getCreatedAt());

        return entity;
    }

    public static Todo toDomain(TodoEntity entity){
        Todo todo = new Todo();
        todo.setUuid(entity.getId());
        todo.setTitle(entity.getTitle());
        todo.setDescription(entity.getDescription());
        todo.setCompleted(entity.isCompleted());
        todo.setCreatedAt(entity.getCreatedAt());
        if (entity.getOwner() != null) {
            todo.setOwnerId(entity.getOwner().getId());
        }

        // Mapear comentarios
        if (entity.getComments() != null && !entity.getComments().isEmpty()) {
            todo.setComments(
                    entity.getComments().stream()
                            .map(CommentMapper::toDomain)
                            .collect(Collectors.toList())
            );
        }

        // Mapear categorías
        if (entity.getCategories() != null && !entity.getCategories().isEmpty()) {
            todo.setCategories(
                    entity.getCategories().stream()
                            .map(CategoryMapper::toDomain)
                            .collect(Collectors.toSet())
            );
        }

        return todo;
    }
}