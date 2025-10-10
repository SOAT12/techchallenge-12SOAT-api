package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.StockMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class StockRepositoryImpl implements StockRepository {

    private final StockJpaRepository stockJpaRepository;
    private final StockMapper stockMapper;

    public StockRepositoryImpl(StockJpaRepository stockJpaRepository) {
        this.stockJpaRepository = stockJpaRepository;
        this.stockMapper = new StockMapper();
    }

    /**
     * @param stock
     * @return Stock
     */
    @Override
    public Stock save(Stock stock) {
        var entity = stockMapper.toEntity(stock);
        var savedStock = stockJpaRepository.save(entity);
        return stockMapper.toDomain(savedStock);
    }

    /**
     * @param stockItemId
     * @return Optional<Stock>
     */
    @Override
    public Optional<Stock> findById(UUID stockItemId) {
        return stockJpaRepository.findById(stockItemId)
                .map(stockMapper::toDomain);
    }

    /**
     * @param stockItemId
     * @return Optional<Stock>
     */
    @Override
    public Optional<Stock> findActiveById(UUID stockItemId) {
        return stockJpaRepository.findByIdAndActiveTrue(stockItemId)
                .map(stockMapper::toDomain);
    }

    /**
     * @param name
     * @return Optional<Stock>
     */
    @Override
    public Optional<Stock> findByName(String name) {
        return stockJpaRepository.findByToolName(name)
                .map(stockMapper::toDomain);
    }

    /**
     * @return List<Stock>
     */
    @Override
    public List<Stock> findAllActive() {
        return stockJpaRepository.findByActiveTrue().stream()
                .map(stockMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * @return
     */
    @Override
    public List<Stock> findAll() {
        return stockJpaRepository.findAll().stream().map(stockMapper::toDomain).toList();
    }
}
