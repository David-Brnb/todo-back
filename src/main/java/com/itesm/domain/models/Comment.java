package com.itesm.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private User author;
    private UUID todoId;

    public Comment() {}

    public Comment(UUID id, String content, LocalDateTime createdAt, User author, UUID todoId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.todoId = todoId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public UUID getTodoId() {
        return todoId;
    }

    public void setTodoId(UUID todoId) {
        this.todoId = todoId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", author=" + author +
                ", todoId=" + todoId +
                '}';
    }
}

