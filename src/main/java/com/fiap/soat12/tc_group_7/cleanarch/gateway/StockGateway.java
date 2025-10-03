package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class StockGateway {
    
    private final StockRepository stockRepository;


    public Optional<Stock> findByName(String toolName) {
        return stockRepository.findByName(toolName);
    }

    public Stock save(Stock newStock) {
        return stockRepository.save(newStock);
    }

    public Optional<Stock> findActiveById(UUID id) {
        return stockRepository.findActiveById(id);
    }

    public void inactivateById(UUID id) {
        stockRepository.inactivateById(id);
    }
}
