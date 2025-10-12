package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
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

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public List<Stock> findAllActive() {
        return stockRepository.findAllActive();
    }

    public Optional<Stock> findById(UUID id) {
        return stockRepository.findById(id);
    }
}
