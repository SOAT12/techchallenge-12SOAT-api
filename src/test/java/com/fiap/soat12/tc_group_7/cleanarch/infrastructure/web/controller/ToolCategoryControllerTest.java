package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ToolCategoryControllerTest {

    @Mock
    private ToolCategoryUseCase toolCategoryUseCase;

    @Mock
    private ToolCategoryPresenter toolCategoryPresenter;

    @InjectMocks
    private ToolCategoryController toolCategoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CreateToolCategory {
        @Test
        void shouldCreateToolCategory() {
            // Arrange
            ToolCategoryRequestDTO dto = new ToolCategoryRequestDTO();
            when(toolCategoryUseCase.createToolCategory(any())).thenReturn(new ToolCategory(UUID.randomUUID(), "Test", true));
            when(toolCategoryPresenter.toToolCategoryResponseDTO(any())).thenReturn(new ToolCategoryResponseDTO());

            // Act
            ToolCategoryResponseDTO result = toolCategoryController.createToolCategory(dto);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class GetToolCategoryById {
        @Test
        void shouldGetToolCategoryById() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(toolCategoryUseCase.getToolCategoryById(id)).thenReturn(new ToolCategory(UUID.randomUUID(), "Test", true));
            when(toolCategoryPresenter.toToolCategoryResponseDTO(any())).thenReturn(new ToolCategoryResponseDTO());

            // Act
            ToolCategoryResponseDTO result = toolCategoryController.getToolCategoryById(id);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class GetAllToolCategories {
        @Test
        void shouldGetAllToolCategories() {
            // Arrange
            when(toolCategoryUseCase.getAllToolCategories()).thenReturn(List.of(new ToolCategory(UUID.randomUUID(), "Test", true)));
            when(toolCategoryPresenter.toToolCategoryResponseDTO(any())).thenReturn(new ToolCategoryResponseDTO());

            // Act
            List<ToolCategoryResponseDTO> result = toolCategoryController.getAllToolCategories();

            // Assert
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class GetAllToolCategoriesActive {
        @Test
        void shouldGetAllToolCategoriesActive() {
            // Arrange
            when(toolCategoryUseCase.getAllToolCategoriesActive()).thenReturn(List.of(new ToolCategory(UUID.randomUUID(), "Test", true)));
            when(toolCategoryPresenter.toToolCategoryResponseDTO(any())).thenReturn(new ToolCategoryResponseDTO());

            // Act
            List<ToolCategoryResponseDTO> result = toolCategoryController.getAllToolCategoriesActive();

            // Assert
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class UpdateToolCategory {
        @Test
        void shouldUpdateToolCategory() {
            // Arrange
            UUID id = UUID.randomUUID();
            ToolCategoryRequestDTO dto = new ToolCategoryRequestDTO();
            when(toolCategoryUseCase.updateToolCategory(any(), any())).thenReturn(new ToolCategory(UUID.randomUUID(), "Test", true));
            when(toolCategoryPresenter.toToolCategoryResponseDTO(any())).thenReturn(new ToolCategoryResponseDTO());

            // Act
            ToolCategoryResponseDTO result = toolCategoryController.updateToolCategory(id, dto);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class ReactivateToolCategory {
        @Test
        void shouldReactivateToolCategory() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(toolCategoryUseCase.reactivateToolCategory(id)).thenReturn(new ToolCategory(UUID.randomUUID(), "Test", true));
            when(toolCategoryPresenter.toToolCategoryResponseDTO(any())).thenReturn(new ToolCategoryResponseDTO());

            // Act
            ToolCategoryResponseDTO result = toolCategoryController.reactivateToolCategory(id);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class LogicallyDeleteToolCategory {
        @Test
        void shouldLogicallyDeleteToolCategory() {
            // Arrange
            UUID id = UUID.randomUUID();

            // Act
            toolCategoryController.logicallyDeleteToolCategory(id);

            // Assert
            verify(toolCategoryUseCase).logicallyDeleteToolCategory(id);
        }
    }
}