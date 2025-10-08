package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import org.aspectj.lang.annotation.Before;

import java.util.List;
import java.util.UUID;

public class StockController {

    private final StockRepository stockRepository;
    private final ToolCategoryRepository toolCategoryRepository;
    private final StockPresenter stockPresenter;

    public StockController(StockRepository stockRepository, ToolCategoryRepository toolCategoryRepository) {
        this.stockRepository = stockRepository;
        this.toolCategoryRepository = toolCategoryRepository;
        this.stockPresenter = new StockPresenter(new ToolCategoryPresenter());
    }
    
    public StockResponseDTO createStock(StockRequestDTO requestDTO) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        Stock stock = stockUseCase.createStock(requestDTO);
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO getStockById(UUID id) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        Stock stock = stockUseCase.findStockItemById(id);
        return stockPresenter.toStockResponseDTO(stock);
    }

    public List<StockResponseDTO> getAllStockItems() {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        List<Stock> allStock = stockUseCase.getAllStock();
        return allStock.stream().map(stockPresenter::toStockResponseDTO).toList();
    }

    public List<StockResponseDTO> getAllStockItemsActive() {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        List<Stock> allActiveStockItems = stockUseCase.getAllActiveStockItems();
        return allActiveStockItems.stream().map(stockPresenter::toStockResponseDTO).toList();
    }

    public StockResponseDTO updateStock(UUID id, StockRequestDTO requestDTO) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        Stock stock = stockUseCase.updateStockItem(id, requestDTO);
        return stockPresenter.toStockResponseDTO(stock);
    }

    public StockResponseDTO reactivateStock(UUID id) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        Stock reactivatedStockItem = stockUseCase.reactivateStockItem(id);
        return stockPresenter.toStockResponseDTO(reactivatedStockItem);
    }

    public void logicallyDeleteStock(UUID id) {
        StockGateway stockGateway = new StockGateway(this.stockRepository);
        ToolCategoryGateway toolCategoryGateway = new ToolCategoryGateway(this.toolCategoryRepository);
        StockUseCase stockUseCase = new StockUseCase(stockGateway, toolCategoryGateway, stockPresenter);

        stockUseCase.inactivateStockItem(id);
    }
}
