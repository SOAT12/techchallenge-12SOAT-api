package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ToolCategoryMapperTest {

    private final ToolCategoryMapper mapper = new ToolCategoryMapper();

    @Test
    void toDomain_WithNullEntity_ShouldReturnNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_WithValidEntity_ShouldReturnDomainObject() {
        UUID id = UUID.randomUUID();
        ToolCategoryEntity entity = ToolCategoryEntity.builder()
                .id(id)
                .toolCategoryName("Test Name")
                .active(true)
                .build();

        ToolCategory domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Test Name", domain.getToolCategoryName());
        assertTrue(domain.getActive());
    }

    @Test
    void toEntity_WithNullDomain_ShouldReturnNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_WithValidDomain_ShouldReturnEntityObject() {
        UUID id = UUID.randomUUID();
        ToolCategory domain = new ToolCategory(id, "Test Name", false);

        ToolCategoryEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Test Name", entity.getToolCategoryName());
        assertFalse(entity.getActive());
    }
}
