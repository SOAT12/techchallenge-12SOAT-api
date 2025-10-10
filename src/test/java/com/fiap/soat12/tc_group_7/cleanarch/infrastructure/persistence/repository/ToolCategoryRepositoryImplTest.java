package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.ToolCategoryMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ToolCategoryRepositoryImplTest {

    @Mock
    private ToolCategoryJpaRepository toolCategoryJpaRepository;

    // We don't mock the mapper, we use a real instance since it's stateless
    private ToolCategoryRepositoryImpl toolCategoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        toolCategoryRepository = new ToolCategoryRepositoryImpl(toolCategoryJpaRepository);
    }

    @Test
    void findById_ShouldReturnDomainObject() {
        UUID id = UUID.randomUUID();
        ToolCategoryEntity entity = ToolCategoryEntity.builder().id(id).toolCategoryName("Test").active(true).build();
        when(toolCategoryJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<ToolCategory> result = toolCategoryRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(toolCategoryJpaRepository, times(1)).findById(id);
    }

    @Test
    void findByToolCategoryName_ShouldReturnDomainObject() {
        String name = "Test";
        ToolCategoryEntity entity = ToolCategoryEntity.builder().id(UUID.randomUUID()).toolCategoryName(name).active(true).build();
        when(toolCategoryJpaRepository.findByToolCategoryName(name)).thenReturn(Optional.of(entity));

        Optional<ToolCategory> result = toolCategoryRepository.findByToolCategoryName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getToolCategoryName());
        verify(toolCategoryJpaRepository, times(1)).findByToolCategoryName(name);
    }

    @Test
    void findAllActive_ShouldReturnListOfDomainObjects() {
        ToolCategoryEntity entity = ToolCategoryEntity.builder().id(UUID.randomUUID()).toolCategoryName("Test").active(true).build();
        when(toolCategoryJpaRepository.findByActiveTrue()).thenReturn(Collections.singletonList(entity));

        List<ToolCategory> result = toolCategoryRepository.findAllActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(toolCategoryJpaRepository, times(1)).findByActiveTrue();
    }

    @Test
    void findAll_ShouldReturnListOfDomainObjects() {
        ToolCategoryEntity entity = ToolCategoryEntity.builder().id(UUID.randomUUID()).toolCategoryName("Test").active(true).build();
        when(toolCategoryJpaRepository.findAll()).thenReturn(Collections.singletonList(entity));

        List<ToolCategory> result = toolCategoryRepository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(toolCategoryJpaRepository, times(1)).findAll();
    }

    @Test
    void save_ShouldMapAndCallJpaSave() {
        UUID id = UUID.randomUUID();
        ToolCategory domain = new ToolCategory(id, "Test to Save", true);
        ToolCategoryEntity entity = new ToolCategoryMapper().toEntity(domain);

        when(toolCategoryJpaRepository.save(any(ToolCategoryEntity.class))).thenReturn(entity);

        ToolCategory result = toolCategoryRepository.save(domain);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(toolCategoryJpaRepository, times(1)).save(any(ToolCategoryEntity.class));
    }
}
