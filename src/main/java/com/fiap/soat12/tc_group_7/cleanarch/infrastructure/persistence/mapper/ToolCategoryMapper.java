package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;

public class ToolCategoryMapper {

    public ToolCategory toDomain(ToolCategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ToolCategory(
                entity.getId(),
                entity.getToolCategoryName(),
                entity.getActive());
    }

    public ToolCategoryEntity toEntity(ToolCategory domain) {
        if (domain == null) {
            return null;
        }
        return ToolCategoryEntity.builder()
                .id(domain.getId())
                .toolCategoryName(domain.getToolCategoryName())
                .active(domain.getActive())
                .build();
    }
}
