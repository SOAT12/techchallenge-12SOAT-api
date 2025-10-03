package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {

    Stock save(Stock stock);

    Optional<Stock> findById(UUID stockItemId);

    List<Stock> findAllActive();

    Optional<Stock> findActiveById(UUID stockItemId);

    Optional<Stock> findByName(String name);

    void inactivateById(UUID stockItemId);

    void activateById(UUID stockItemId);
}
