package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.demo.TodoView;
import com.itesm.infrastructure.persistence.entity.CategoryEntity;
import com.itesm.infrastructure.persistence.entity.CommentEntity;
import com.itesm.infrastructure.persistence.entity.TodoEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TodoViewMapper {

    private TodoViewMapper() {}

    /**
     * View sin tocar relaciones lazy — solo campos propios del todo.
     */
    public static TodoView toShallowView(TodoEntity t) {
        return new TodoView(
                t.getId().toString(),
                t.getTitle(),
                t.isCompleted(),
                null,
                null,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    /**
     * View completa — acceso a owner, categories y comments.
     * Si las relaciones son LAZY y no fueron fetch-eadas, este método dispara N+1.
     */
    public static TodoView toFullView(TodoEntity t) {
        List<String> categories = t.getCategories().stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());

        List<TodoView.CommentView> comments = t.getComments().stream()
                .map(TodoViewMapper::toCommentView)
                .collect(Collectors.toList());

        return new TodoView(
                t.getId().toString(),
                t.getTitle(),
                t.isCompleted(),
                t.getOwner().getFullName(),
                t.getOwner().getEmail(),
                categories,
                comments
        );
    }

    private static TodoView.CommentView toCommentView(CommentEntity c) {
        String author = c.getAuthor() != null ? c.getAuthor().getFullName() : "desconocido";
        return new TodoView.CommentView(c.getContent(), author);
    }
}