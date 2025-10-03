package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class StockPresenter {

    public StockResponseDTO toStockResponseDTO(Stock stock) {
        return StockResponseDTO.builder()
                .id(stock.getId())
                .toolName(stock.getToolName())
                .value(stock.getValue())
                .isActive(stock.isActive())
                .quantity(stock.getQuantity())
                .created_at(stock.getCreatedAt())
                .updated_at(stock.getUpdatedAt())
                .toolCategory(stock.getToolCategory())
                .build();

    }

    public StockUseCase.CreateStockCommand toCreateStockCommand(StockRequestDTO dto) {

    }
}
