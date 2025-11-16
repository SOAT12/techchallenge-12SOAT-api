package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.CreateStockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;

import java.util.List;
import java.util.UUID;

public class StockController {

    private final StockUseCase stockUseCase;
    private final StockPresenter stockPresenter;

    public StockController(StockUseCase stockUseCase, StockPresenter stockPresenter) {
        this.stockUseCase = stockUseCase;
        this.stockPresenter = stockPresenter;
    }
    
    public StockResponseDTO createStock(CreateStockRequestDTO dto) {
        Stock stock = stockUseCase.createStock(dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getToolCategoryId());
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO getStockById(UUID id) {
        Stock stock = stockUseCase.findStockItemById(id);
        return stockPresenter.toStockResponseDTO(stock);
    }

    public List<StockResponseDTO> getAllStockItems() {
        List<Stock> allStock = stockUseCase.getAllStock();
        return allStock.stream().map(stockPresenter::toStockResponseDTO).toList();
    }

    public List<StockResponseDTO> getAllStockItemsActive() {
        List<Stock> allActiveStockItems = stockUseCase.getAllActiveStockItems();
        return allActiveStockItems.stream().map(stockPresenter::toStockResponseDTO).toList();
    }

    public StockResponseDTO updateStock(UUID id, StockRequestDTO dto) {
        Stock stock = stockUseCase.updateStockItem(id, dto.getToolName(), dto.getValue(), dto.getQuantity(), dto.getActive(), dto.getToolCategoryId());
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO reactivateStock(UUID id) {
        Stock reactivatedStockItem = stockUseCase.reactivateStockItem(id);
        return stockPresenter.toStockResponseDTO(reactivatedStockItem);
    }

    public void logicallyDeleteStock(UUID id) {
        stockUseCase.inactivateStockItem(id);
    }
}
