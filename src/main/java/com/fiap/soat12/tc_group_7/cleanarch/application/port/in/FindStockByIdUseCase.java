package com.fiap.soat12.tc_group_7.cleanarch.application.port.in;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;

import java.util.Optional;
import java.util.UUID;

public interface FindStockByIdUseCase {
    Optional<Stock> findStockItemById(UUID id);
}
