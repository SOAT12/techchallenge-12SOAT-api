package com.fiap.soat12.tc_group_7.dto.stock;

import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

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
    private Date created_at;
    private Date updated_at;
    private ToolCategoryResponseDTO toolCategory;
}
