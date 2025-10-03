package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.SpringStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockRepositoryAdapter implements StockRepository {

    private final SpringStockRepository springStockRepository;
    private final StockPresenter stockPresenter;

    /**
     * @param stock
     * @return Stock
     */
    @Override
    public Stock save(Stock stock) {
        var entity = stockPresenter.toEntity(stock);
        var savedStock = springStockRepository.save(entity);
        return stockPresenter.toDomain(savedStock);
    }

    /**
     * @param stockItemId
     * @return Optional<Stock>
     */
    @Override
    public Optional<Stock> findById(UUID stockItemId) {
        return springStockRepository.findById(stockItemId)
                .map(stockPresenter::toDomain);
    }

    /**
     * @param stockItemId
     * @return Optional<Stock>
     */
    @Override
    public Optional<Stock> findActiveById(UUID stockItemId) {
        return springStockRepository.findByIdAndActiveTrue(stockItemId)
                .map(stockPresenter::toDomain);
    }

    /**
     * @param name
     * @return Optional<Stock>
     */
    @Override
    public Optional<Stock> findByName(String name) {
        return springStockRepository.findByToolName(name)
                .map(stockPresenter::toDomain);
    }

    /**
     * @return List<Stock>
     */
    @Override
    public List<Stock> findAllActive() {
        return springStockRepository.findByActiveTrue().stream()
                .map(stockPresenter::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * @param stockItemId
     */
    @Override
    public void inactivateById(UUID stockItemId) {
        springStockRepository.findById(stockItemId).ifPresent(entity -> {
            entity.setActive(false);
            springStockRepository.save(entity);
        });
    }

    /**
     * @param stockItemId
     */
    @Override
    public void activateById(UUID stockItemId) {
        springStockRepository.findById(stockItemId).ifPresent(entity -> {
            entity.setActive(true);
            springStockRepository.save(entity);
        });

    }
}
