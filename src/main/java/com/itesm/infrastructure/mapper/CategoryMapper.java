package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Category;
import com.itesm.infrastructure.persistence.entity.CategoryEntity;

public class CategoryMapper {
    
    private CategoryMapper() {}

    /**
     * Convierte CategoryEntity a Category (domain model)
     */
    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getColor()
        );
    }

    /**
     * Convierte Category a CategoryEntity
     */
    public static CategoryEntity toEntity(Category domain) {
        if (domain == null) {
            return null;
        }

        return new CategoryEntity(
                domain.getId(),
                domain.getName(),
                domain.getColor()
        );
    }
}

