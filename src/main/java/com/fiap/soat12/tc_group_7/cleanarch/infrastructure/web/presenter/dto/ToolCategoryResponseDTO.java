package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para respostas de ToolCategory.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCategoryResponseDTO {
    private UUID id;
    private String toolCategoryName;
    private Boolean active;
}
