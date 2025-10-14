package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ToolCategoryUseCase {

    private static final String NOT_FOUND_TOOL_CATEGORY_MSG = "Categoria de peça não encontrada.";

    private final ToolCategoryGateway toolCategoryGateway;

    public ToolCategory createToolCategory(String toolCategoryName) {

        ToolCategory toolCategory = ToolCategory.create(toolCategoryName);
        return toolCategoryGateway.save(toolCategory);
    }

    public ToolCategory updateToolCategory(UUID id, String toolCategoryName) {
        ToolCategory existingCategory = toolCategoryGateway.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_TOOL_CATEGORY_MSG));

        if (!existingCategory.getToolCategoryName().equalsIgnoreCase(toolCategoryName)) {
            toolCategoryGateway.findByName(toolCategoryName).ifPresent(category -> {
                if (!category.getId().equals(id)) {
                    throw new IllegalArgumentException(String.format("O nome da categoria %s já está em uso.", toolCategoryName));
                }
            });
        }

        existingCategory.changeName(toolCategoryName);

        return toolCategoryGateway.save(existingCategory);
    }

    public ToolCategory getToolCategoryById(UUID id) {
        return toolCategoryGateway.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_TOOL_CATEGORY_MSG));
    }

    public List<ToolCategory> getAllToolCategories() {
        return toolCategoryGateway.findAll();
    }

    public List<ToolCategory> getAllToolCategoriesActive() {
        return toolCategoryGateway.findAllActive();
    }

    public void logicallyDeleteToolCategory(UUID id) {
        ToolCategory toolCategory = toolCategoryGateway.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_TOOL_CATEGORY_MSG));

        ToolCategory inactivatedCategory = toolCategory.deactivate();

        toolCategoryGateway.save(inactivatedCategory);
    }

    public ToolCategory reactivateToolCategory(UUID id) {
        ToolCategory toolCategory = toolCategoryGateway.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_TOOL_CATEGORY_MSG));

        ToolCategory activatedCategory = toolCategory.activate();

        return toolCategoryGateway.save(activatedCategory);
    }
}
