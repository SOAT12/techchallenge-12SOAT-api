package com.fiap.soat12.tc_group_7.dto.toolCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respostas de ToolCategory.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolCategoryResponseDTO {
    private Long id;
    private String toolCategoryName;
    private Boolean active;
}
