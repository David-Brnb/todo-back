package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Comment;
import com.itesm.infrastructure.persistence.entity.CommentEntity;

public class CommentMapper {
    
    private CommentMapper() {}

    /**
     * Convierte CommentEntity a Comment (domain model)
     */
    public static Comment toDomain(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Comment(
                entity.getId(),
                entity.getContent(),
                entity.getCreatedAt(),
                entity.getAuthor() != null ? UserMapper.toDomain(entity.getAuthor()) : null,
                entity.getTodo() != null ? entity.getTodo().getId() : null
        );
    }

    /**
     * Convierte Comment a CommentEntity
     */
    public static CommentEntity toEntity(Comment domain) {
        if (domain == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();
        entity.setId(domain.getId());
        entity.setContent(domain.getContent());
        entity.setCreatedAt(domain.getCreatedAt());
        // Nota: author y todo se asignarán en el contexto donde se use

        return entity;
    }
}

