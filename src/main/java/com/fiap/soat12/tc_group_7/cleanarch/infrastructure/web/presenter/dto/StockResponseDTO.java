package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto;

import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * DTO para respostas de Stock.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private UUID id;
    private String toolName;
    private BigDecimal value;
    private Boolean isActive;
    private Integer quantity;
    private Date created_at;
    private Date updated_at;
    private ToolCategoryResponseDTO toolCategory;
}
