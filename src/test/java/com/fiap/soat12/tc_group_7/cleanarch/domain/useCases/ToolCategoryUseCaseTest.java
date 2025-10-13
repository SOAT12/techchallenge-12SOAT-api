package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
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

class ToolCategoryUseCaseTest {

    @Mock
    private ToolCategoryGateway toolCategoryGateway;

    @InjectMocks
    private ToolCategoryUseCase toolCategoryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createToolCategory_ShouldSaveAndReturnCategory() {
        String categoryName = "Drills";
        ToolCategory category = ToolCategory.create(categoryName);
        when(toolCategoryGateway.save(any(ToolCategory.class))).thenReturn(category);

        ToolCategory result = toolCategoryUseCase.createToolCategory(categoryName);

        assertNotNull(result);
        assertEquals(categoryName, result.getToolCategoryName());
        verify(toolCategoryGateway, times(1)).save(any(ToolCategory.class));
    }

    @Test
    void updateToolCategory_WhenExistsAndNameIsUnique_ShouldUpdateAndReturn() {
        UUID id = UUID.randomUUID();
        String newName = "Saws";
        ToolCategory existingCategory = new ToolCategory(id, "Old Saws", true);

        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(existingCategory));
        when(toolCategoryGateway.findByName(newName)).thenReturn(Optional.empty());
        when(toolCategoryGateway.save(any(ToolCategory.class))).thenReturn(existingCategory);

        ToolCategory result = toolCategoryUseCase.updateToolCategory(id, newName);

        assertNotNull(result);
        assertEquals(newName, result.getToolCategoryName());
        verify(toolCategoryGateway, times(1)).save(existingCategory);
    }

    @Test
    void updateToolCategory_WhenNameIsAlreadyInUse_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        String newName = "Saws";
        ToolCategory existingCategory = new ToolCategory(id, "Old Saws", true);
        ToolCategory otherCategoryWithSameName = new ToolCategory(UUID.randomUUID(), newName, true);

        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(existingCategory));
        when(toolCategoryGateway.findByName(newName)).thenReturn(Optional.of(otherCategoryWithSameName));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            toolCategoryUseCase.updateToolCategory(id, newName);
        });

        assertEquals(String.format("O nome da categoria %s já está em uso.", newName), e.getMessage());
        verify(toolCategoryGateway, never()).save(any());
    }

    @Test
    void updateToolCategory_WhenCategoryNotFound_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            toolCategoryUseCase.updateToolCategory(id, "Any Name");
        });
    }

    @Test
    void getToolCategoryById_WhenExists_ShouldReturnCategory() {
        UUID id = UUID.randomUUID();
        ToolCategory category = new ToolCategory(id, "Pliers", true);
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(category));

        ToolCategory result = toolCategoryUseCase.getToolCategoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getToolCategoryById_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            toolCategoryUseCase.getToolCategoryById(id);
        });
    }

    @Test
    void getAllToolCategories_ShouldReturnList() {
        List<ToolCategory> categories = Collections.singletonList(ToolCategory.create("Test"));
        when(toolCategoryGateway.findAll()).thenReturn(categories);

        List<ToolCategory> result = toolCategoryUseCase.getAllToolCategories();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getAllToolCategoriesActive_ShouldReturnList() {
        List<ToolCategory> categories = Collections.singletonList(ToolCategory.create("Test"));
        when(toolCategoryGateway.findAllActive()).thenReturn(categories);

        List<ToolCategory> result = toolCategoryUseCase.getAllToolCategoriesActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void logicallyDeleteToolCategory_WhenExists_ShouldDeactivateAndSave() {
        UUID id = UUID.randomUUID();
        ToolCategory category = new ToolCategory(id, "To Delete", true);
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(category));

        toolCategoryUseCase.logicallyDeleteToolCategory(id);

        assertFalse(category.getActive());
        verify(toolCategoryGateway, times(1)).save(category);
    }

    @Test
    void logicallyDeleteToolCategory_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            toolCategoryUseCase.logicallyDeleteToolCategory(id);
        });
    }

    @Test
    void reactivateToolCategory_WhenExistsAndInactive_ShouldActivateAndSave() {
        UUID id = UUID.randomUUID();
        ToolCategory category = ToolCategory.create("To Reactivate");
        category.deactivate();
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.of(category));

        toolCategoryUseCase.reactivateToolCategory(id);

        assertTrue(category.getActive());
        verify(toolCategoryGateway, times(1)).save(category);
    }

    @Test
    void reactivateToolCategory_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(toolCategoryGateway.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            toolCategoryUseCase.reactivateToolCategory(id);
        });
    }
}
