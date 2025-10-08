package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StockPresenter {

    private final ToolCategoryPresenter toolCategoryPresenter;

    public StockResponseDTO toStockResponseDTO(Stock stock) {
        return StockResponseDTO.builder()
                .id(stock.getId())
                .toolName(stock.getToolName())
                .value(stock.getValue())
                .isActive(stock.isActive())
                .quantity(stock.getQuantity())
                .created_at(stock.getCreatedAt())
                .updated_at(stock.getUpdatedAt())
                .toolCategory(toolCategoryPresenter.toToolCategoryResponseDTO(stock.getToolCategory()))
                .build();

    }

    public Stock toStock(StockRequestDTO stockRequestDTO, ToolCategory toolCategory) {
        return new Stock(
                stockRequestDTO.getToolName(),
                stockRequestDTO.getValue(),
                stockRequestDTO.getQuantity(),
                toolCategory);
    }
}
