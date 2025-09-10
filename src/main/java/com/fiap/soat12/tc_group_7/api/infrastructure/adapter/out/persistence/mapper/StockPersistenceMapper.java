package com.fiap.soat12.tc_group_7.api.infrastructure.adapter.out.persistence.mapper;


import com.fiap.soat12.tc_group_7.api.domain.model.Stock;
import com.fiap.soat12.tc_group_7.api.infrastructure.adapter.out.persistence.entity.StockEntity;
import org.springframework.stereotype.Component;

@Component
public class StockPersistenceMapper {

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
                entity.getActive()
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
                domain.getToolCategory()
        );
    }
}
