package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StockController {

    private final com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository stockRepository;
    private final StockPresenter stockPresenter;

    public StockController(com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.stockPresenter = new StockPresenter();
    }

    public StockResponseDTO createStock(StockRequestDTO requestDTO) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway);

        Stock stock = stockUseCase.createStock(stockPresenter.toCreateStockCommand(requestDTO));
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO getStockById(UUID id) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway);

        Stock stock = stockUseCase.findStockItemById(id);
        return stockPresenter.toStockResponseDTO(stock);
    }

    public List<StockResponseDTO> getAllStockItems() {
        return null;
    }

    public List<StockResponseDTO> getAllStockItemsActive() {
        return null;
    }

    public StockResponseDTO updateStock(Long id, StockRequestDTO requestDTO) {
        return null;
    }

    public StockResponseDTO reactivateStock(Long id) {
        return null;
    }

    public void logicallyDeleteStock(Long id) {
    }
}
