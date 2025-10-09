package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StockUseCase {

    private final StockGateway stockGateway;
    private final ToolCategoryGateway toolCategoryGateway;
    private final StockPresenter stockPresenter;

    public List<Stock> getAllStock(){
        return stockGateway.findAll();
    }

    public List<Stock> getAllActiveStockItems(){
        return stockGateway.findAllActive();
    }

    public Stock createStock(StockRequestDTO stockDto) {
        ToolCategory category = toolCategoryGateway.findById(stockDto.getToolCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        stockGateway.findByName(stockDto.getToolName()).ifPresent(existingItem -> {
            throw new IllegalArgumentException("Item já cadastrado.");
        });

        Stock newStock = stockPresenter.toStock(stockDto, category);
        return stockGateway.save(newStock);
    }

    public Stock updateStockItem(UUID id, StockRequestDTO stockDto) {
        Stock existingItem = stockGateway.findById(id).orElseThrow(() -> new NotFoundException("Item não encontrado."));

        if (!existingItem.getToolName().equalsIgnoreCase(stockDto.getToolName())) {
            stockGateway.findByName(stockDto.getToolName()).ifPresent(item -> {
                if (!item.getId().equals(id)) {
                    throw new IllegalArgumentException("O nome da ferramenta '" + stockDto.getToolName() + "' já está em uso.");
                }
            });
        }

        ToolCategory category = toolCategoryGateway.findById(stockDto.getToolCategoryId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        existingItem.updateDetails(stockDto.getToolName(), stockDto.getValue(), category);

        int quantityDifference = stockDto.getQuantity() - existingItem.getQuantity();

        if (quantityDifference > 0) {
            existingItem.addStock(quantityDifference);
        } else if (quantityDifference < 0) {
            existingItem.removingStock(Math.abs(quantityDifference));
        }

        return stockGateway.save(existingItem);
    }

    public Stock findStockItemById(UUID id) {
        return stockGateway.findActiveById(id).orElseThrow(() -> new NotFoundException("Item de estoque não encontrado."));
    }

    public void inactivateStockItem(UUID id) {
        Stock stock = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado para deletar/inativar."));

        Stock inactivatedStock = stock.deactivate();
        stockGateway.save(inactivatedStock);
    }

    public Stock reactivateStockItem(UUID id) {
        Stock stock = stockGateway.findById(id)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado para reativar."));

        if (Boolean.TRUE.equals(stock.isActive())) {
            throw new IllegalStateException("O item de estoque já está ativo.");
        }

        stock.updateDetails(stock.getToolName(), stock.getValue(), stock.getToolCategory());

        return stockGateway.save(stock);
    }
}
