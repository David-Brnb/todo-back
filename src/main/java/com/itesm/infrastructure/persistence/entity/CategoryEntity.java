package com.itesm.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 80, unique = true)
    private String name;

    @Column(length = 20)
    private String color;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<TodoEntity> todos = new HashSet<>();

    public CategoryEntity() {}

    public CategoryEntity(UUID id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Set<TodoEntity> getTodos() { return todos; }
    public void setTodos(Set<TodoEntity> todos) { this.todos = todos; }
}