package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;

import java.util.List;
import java.util.UUID;

public class ToolCategoryController {

    private final ToolCategoryUseCase toolCategoryUseCase;
    private final ToolCategoryPresenter toolCategoryPresenter;

    public ToolCategoryController(ToolCategoryUseCase toolCategoryUseCase, ToolCategoryPresenter toolCategoryPresenter) {
        this.toolCategoryUseCase = toolCategoryUseCase;
        this.toolCategoryPresenter = toolCategoryPresenter;
    }

    public ToolCategoryResponseDTO createToolCategory(ToolCategoryRequestDTO requestDTO) {
        ToolCategory toolCategory = toolCategoryUseCase.createToolCategory(requestDTO.getToolCategoryName());
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public ToolCategoryResponseDTO getToolCategoryById(UUID id) {
        ToolCategory toolCategoryById = toolCategoryUseCase.getToolCategoryById(id);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategoryById);
    }

    public List<ToolCategoryResponseDTO> getAllToolCategories() {
        List<ToolCategory> allToolCategories = toolCategoryUseCase.getAllToolCategories();
        return allToolCategories.stream().map(toolCategoryPresenter::toToolCategoryResponseDTO).toList();
    }

    public List<ToolCategoryResponseDTO> getAllToolCategoriesActive() {
        List<ToolCategory> allToolCategoriesActive = toolCategoryUseCase.getAllToolCategoriesActive();
        return allToolCategoriesActive.stream().map(toolCategoryPresenter::toToolCategoryResponseDTO).toList();
    }

    public ToolCategoryResponseDTO updateToolCategory(UUID id, ToolCategoryRequestDTO requestDTO) {
        ToolCategory toolCategory = toolCategoryUseCase.updateToolCategory(id, requestDTO.getToolCategoryName());
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public ToolCategoryResponseDTO reactivateToolCategory(UUID id) {
        ToolCategory toolCategory = toolCategoryUseCase.reactivateToolCategory(id);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public void logicallyDeleteToolCategory(UUID id) {
        toolCategoryUseCase.logicallyDeleteToolCategory(id);
    }
}
