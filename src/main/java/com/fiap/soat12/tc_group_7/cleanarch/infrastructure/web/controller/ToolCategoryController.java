package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;

import java.util.List;
import java.util.UUID;

public class ToolCategoryController {

    private final ToolCategoryRepository toolCategoryRepository;
    private final ToolCategoryPresenter toolCategoryPresenter;

    public ToolCategoryController(ToolCategoryRepository toolCategoryRepository) {
        this.toolCategoryRepository = toolCategoryRepository;
        this.toolCategoryPresenter = new ToolCategoryPresenter();
    }

    public ToolCategoryResponseDTO createToolCategory(ToolCategoryRequestDTO requestDTO) {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        ToolCategory toolCategory = toolCategoryUseCase.createToolCategory(requestDTO);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public ToolCategoryResponseDTO getToolCategoryById(UUID id) {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        ToolCategory toolCategoryById = toolCategoryUseCase.getToolCategoryById(id);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategoryById);
    }

    public List<ToolCategoryResponseDTO> getAllToolCategories() {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        List<ToolCategory> allToolCategories = toolCategoryUseCase.getAllToolCategories();
        return allToolCategories.stream().map(toolCategoryPresenter::toToolCategoryResponseDTO).toList();
    }

    public List<ToolCategoryResponseDTO> getAllToolCategoriesActive() {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        List<ToolCategory> allToolCategoriesActive = toolCategoryUseCase.getAllToolCategoriesActive();
        return allToolCategoriesActive.stream().map(toolCategoryPresenter::toToolCategoryResponseDTO).toList();
    }

    public ToolCategoryResponseDTO updateToolCategory(UUID id, ToolCategoryRequestDTO requestDTO) {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        ToolCategory toolCategory = toolCategoryUseCase.updateToolCategory(id, requestDTO);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public ToolCategoryResponseDTO reactivateToolCategory(UUID id) {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        ToolCategory toolCategory = toolCategoryUseCase.reactivateToolCategory(id);
        return toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory);
    }

    public void logicallyDeleteToolCategory(UUID id) {
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        ToolCategoryUseCase toolCategoryUseCase = new ToolCategoryUseCase(toolCategoryGateway, toolCategoryPresenter);

        toolCategoryUseCase.logicallyDeleteToolCategory(id);
    }
}
