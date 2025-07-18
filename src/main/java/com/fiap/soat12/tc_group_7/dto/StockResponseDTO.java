package com.fiap.soat12.tc_group_7.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO para respostas de Stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private Long id;
    private String toolName;
    private BigDecimal value;
    private Boolean active;
    private Integer quantity;
    private ToolCategoryResponseDTO toolCategory;
}
