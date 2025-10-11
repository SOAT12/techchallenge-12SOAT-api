package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StockUseCase {

    private static final String NOT_FOUND_STOCK_ITEM_MSG = "Item de estoque não encontrado.";

    private final StockGateway stockGateway;
    private final ToolCategoryGateway toolCategoryGateway;

    public Stock createStock(String toolName, BigDecimal value, Integer quantity, UUID toolCategoryId) {
        ToolCategory category = toolCategoryGateway.findById(toolCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        stockGateway.findByName(toolName).ifPresent(existingItem -> {
            throw new IllegalArgumentException("Item já cadastrado.");
        });

        Stock newStock = Stock.create(toolName, value, quantity, category);
        return stockGateway.save(newStock);
    }

    public Stock updateStockItem(UUID id, String toolName, BigDecimal value, Integer quantity, Boolean isActive,  UUID toolCategoryId) {
        Stock existingItem = stockGateway.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));

        if (!existingItem.getToolName().equalsIgnoreCase(toolName)) {
            stockGateway.findByName(toolName).ifPresent(item -> {
                if (!item.getId().equals(id)) {
                    throw new IllegalArgumentException(String.format("O nome da ferramenta %s já está em uso.", toolName));
                }
            });
        }

        ToolCategory category = toolCategoryGateway.findById(toolCategoryId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        existingItem.updateDetails(toolName, value, isActive, category);

        int quantityDifference = quantity - existingItem.getQuantity();

        if (quantityDifference > 0) {
            existingItem.addStock(quantityDifference);
        } else if (quantityDifference < 0) {
            existingItem.removingStock(Math.abs(quantityDifference));
        }

        return stockGateway.save(existingItem);
    }

    public List<Stock> getAllStock(){
        return stockGateway.findAll();
    }

    public List<Stock> getAllActiveStockItems(){
        return stockGateway.findAllActive();
    }

    public Stock findStockItemById(UUID id) {
        return stockGateway.findActiveById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));
    }

    public void inactivateStockItem(UUID id) {
        Stock stock = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));

        Stock inactivatedStock = stock.deactivate();
        stockGateway.save(inactivatedStock);
    }

    public Stock reactivateStockItem(UUID id) {
        Stock stock = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STOCK_ITEM_MSG));

        Stock activatedStock = stock.activate();
        return stockGateway.save(activatedStock);
    }
}
