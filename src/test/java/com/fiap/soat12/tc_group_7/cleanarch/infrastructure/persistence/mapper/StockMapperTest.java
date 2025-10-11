package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.StockEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockMapperTest {

    private StockMapper stockMapper;
    private ToolCategoryEntity toolCategoryEntity;
    private ToolCategory toolCategoryDomain;
    private UUID categoryId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        stockMapper = new StockMapper();
        toolCategoryEntity = ToolCategoryEntity.builder()
                .id(categoryId)
                .toolCategoryName("Test Category")
                .active(true)
                .build();
        toolCategoryDomain = new ToolCategory(categoryId, "Test Category", true);
    }

    @Test
    void toDomain_WithNullEntity_ShouldReturnNull() {
        assertNull(stockMapper.toDomain(null));
    }

    @Test
    void toDomain_WithValidEntity_ShouldReturnDomainObject() {
        UUID stockId = UUID.randomUUID();
        Date now = new Date();

        StockEntity entity = new StockEntity();
        entity.setId(stockId);
        entity.setToolName("Test Stock");
        entity.setValue(BigDecimal.TEN);
        entity.setQuantity(100);
        entity.setActive(true);
        entity.setToolCategoryEntity(toolCategoryEntity);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        Stock domain = stockMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(stockId, domain.getId());
        assertEquals("Test Stock", domain.getToolName());
        assertEquals(0, BigDecimal.TEN.compareTo(domain.getValue()));
        assertEquals(100, domain.getQuantity());
        assertTrue(domain.isActive());
        assertEquals(now, domain.getCreatedAt());
        assertEquals(now, domain.getUpdatedAt());
        assertNotNull(domain.getToolCategory());
        assertEquals(categoryId, domain.getToolCategory().getId());
    }

    @Test
    void toEntity_WithNullDomain_ShouldReturnNull() {
        assertNull(stockMapper.toEntity(null));
    }

    @Test
    void toEntity_WithValidDomain_ShouldReturnEntityObject() {
        UUID stockId = UUID.randomUUID();
        Date now = new Date();
        Stock domain = new Stock(stockId, "Test Stock", BigDecimal.TEN, 100, toolCategoryDomain, false, now, now);

        StockEntity entity = stockMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(stockId, entity.getId());
        assertEquals("Test Stock", entity.getToolName());
        assertEquals(0, BigDecimal.TEN.compareTo(entity.getValue()));
        assertEquals(100, entity.getQuantity());
        assertFalse(entity.getActive());
        assertNotNull(entity.getToolCategoryEntity());
        assertEquals(categoryId, entity.getToolCategoryEntity().getId());
        // Note: Audit fields (createdAt, updatedAt) are not mapped from domain to entity
        // as they are handled by @PrePersist/@PreUpdate in the entity itself.
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
    }
}
