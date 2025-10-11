package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void createToolCategory_ShouldDelegateToUseCaseAndPresenter() {
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("New Category");
        ToolCategory domainObject = ToolCategory.create("New Category");
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryUseCase.createToolCategory("New Category")).thenReturn(domainObject);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(domainObject)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryController.createToolCategory(requestDTO);

        assertNotNull(result);
        verify(toolCategoryUseCase, times(1)).createToolCategory("New Category");
        verify(toolCategoryPresenter, times(1)).toToolCategoryResponseDTO(domainObject);
    }

    @Test
    void getToolCategoryById_ShouldDelegateToUseCaseAndPresenter() {
        UUID id = UUID.randomUUID();
        ToolCategory domainObject = new ToolCategory(id, "Test", true);
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryUseCase.getToolCategoryById(id)).thenReturn(domainObject);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(domainObject)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryController.getToolCategoryById(id);

        assertNotNull(result);
        verify(toolCategoryUseCase, times(1)).getToolCategoryById(id);
        verify(toolCategoryPresenter, times(1)).toToolCategoryResponseDTO(domainObject);
    }

    @Test
    void getAllToolCategories_ShouldReturnListOfDTOs() {
        ToolCategory domainObject = ToolCategory.create("Test");
        List<ToolCategory> domainList = Collections.singletonList(domainObject);
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryUseCase.getAllToolCategories()).thenReturn(domainList);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(domainObject)).thenReturn(responseDTO);

        List<ToolCategoryResponseDTO> result = toolCategoryController.getAllToolCategories();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(toolCategoryUseCase, times(1)).getAllToolCategories();
    }

    @Test
    void getAllToolCategoriesActive_ShouldReturnListOfDTOs() {
        ToolCategory domainObject = ToolCategory.create("Test");
        List<ToolCategory> domainList = Collections.singletonList(domainObject);
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryUseCase.getAllToolCategoriesActive()).thenReturn(domainList);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(domainObject)).thenReturn(responseDTO);

        List<ToolCategoryResponseDTO> result = toolCategoryController.getAllToolCategoriesActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(toolCategoryUseCase, times(1)).getAllToolCategoriesActive();
    }

    @Test
    void updateToolCategory_ShouldDelegateToUseCaseAndPresenter() {
        UUID id = UUID.randomUUID();
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Updated Name");
        ToolCategory domainObject = new ToolCategory(id, "Updated Name", true);
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryUseCase.updateToolCategory(id, "Updated Name")).thenReturn(domainObject);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(domainObject)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryController.updateToolCategory(id, requestDTO);

        assertNotNull(result);
        verify(toolCategoryUseCase, times(1)).updateToolCategory(id, "Updated Name");
        verify(toolCategoryPresenter, times(1)).toToolCategoryResponseDTO(domainObject);
    }

    @Test
    void reactivateToolCategory_ShouldDelegateToUseCaseAndPresenter() {
        UUID id = UUID.randomUUID();
        ToolCategory domainObject = new ToolCategory(id, "Reactivated", true);
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryUseCase.reactivateToolCategory(id)).thenReturn(domainObject);
        when(toolCategoryPresenter.toToolCategoryResponseDTO(domainObject)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryController.reactivateToolCategory(id);

        assertNotNull(result);
        verify(toolCategoryUseCase, times(1)).reactivateToolCategory(id);
        verify(toolCategoryPresenter, times(1)).toToolCategoryResponseDTO(domainObject);
    }

    @Test
    void logicallyDeleteToolCategory_ShouldCallUseCase() {
        UUID id = UUID.randomUUID();
        doNothing().when(toolCategoryUseCase).logicallyDeleteToolCategory(id);

        toolCategoryController.logicallyDeleteToolCategory(id);

        verify(toolCategoryUseCase, times(1)).logicallyDeleteToolCategory(id);
    }
}
