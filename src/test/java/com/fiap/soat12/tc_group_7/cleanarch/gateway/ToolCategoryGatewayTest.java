package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
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
import static org.mockito.Mockito.*;

class ToolCategoryGatewayTest {

    @Mock
    private ToolCategoryRepository toolCategoryRepository;

    @InjectMocks
    private ToolCategoryGateway toolCategoryGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();
        ToolCategory category = new ToolCategory(id, "Test", true);
        when(toolCategoryRepository.findById(id)).thenReturn(Optional.of(category));

        Optional<ToolCategory> result = toolCategoryGateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(toolCategoryRepository, times(1)).findById(id);
    }

    @Test
    void save_ShouldCallRepository() {
        ToolCategory category = ToolCategory.create("New Category");
        when(toolCategoryRepository.save(category)).thenReturn(category);

        ToolCategory result = toolCategoryGateway.save(category);

        assertNotNull(result);
        verify(toolCategoryRepository, times(1)).save(category);
    }

    @Test
    void findByName_ShouldCallRepository() {
        String name = "Test Category";
        ToolCategory category = ToolCategory.create(name);
        when(toolCategoryRepository.findByToolCategoryName(name)).thenReturn(Optional.of(category));

        Optional<ToolCategory> result = toolCategoryGateway.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getToolCategoryName());
        verify(toolCategoryRepository, times(1)).findByToolCategoryName(name);
    }

    @Test
    void findAll_ShouldCallRepository() {
        List<ToolCategory> categories = Collections.singletonList(ToolCategory.create("Test"));
        when(toolCategoryRepository.findAll()).thenReturn(categories);

        List<ToolCategory> result = toolCategoryGateway.findAll();

        assertFalse(result.isEmpty());
        verify(toolCategoryRepository, times(1)).findAll();
    }

    @Test
    void findAllActive_ShouldCallRepository() {
        List<ToolCategory> categories = Collections.singletonList(ToolCategory.create("Test"));
        when(toolCategoryRepository.findAllActive()).thenReturn(categories);

        List<ToolCategory> result = toolCategoryGateway.findAllActive();

        assertFalse(result.isEmpty());
        verify(toolCategoryRepository, times(1)).findAllActive();
    }
}
