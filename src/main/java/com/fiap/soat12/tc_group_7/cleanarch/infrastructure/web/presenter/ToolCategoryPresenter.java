package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;

public class ToolCategoryPresenter {
    public ToolCategoryResponseDTO toToolCategoryResponseDTO(ToolCategory toolCategory) {
        return ToolCategoryResponseDTO.builder()
                .toolCategoryName(toolCategory.getToolCategoryName())
                .active(toolCategory.getActive())
                .id(toolCategory.getId())
                .build();
    }
}
