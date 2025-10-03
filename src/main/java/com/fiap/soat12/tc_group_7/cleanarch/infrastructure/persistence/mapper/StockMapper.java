package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;


import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.StockEntity;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    // Converts a persistence entity to a domain model
    public Stock toDomain(StockEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Stock(
                entity.getId(),
                entity.getToolName(),
                entity.getValue(),
                entity.getQuantity(),
                entity.getToolCategory(),
                entity.getActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // Converts a domain model to a persistence entity
    public StockEntity toEntity(Stock domain) {
        if (domain == null) {
            return null;
        }
        return new StockEntity(
                domain.getId(),
                domain.getToolName(),
                domain.getValue(),
                domain.isActive(),
                domain.getQuantity(),
                domain.getToolCategory(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
