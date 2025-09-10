package com.fiap.soat12.tc_group_7.api.application.port.in;

import com.fiap.soat12.tc_group_7.api.domain.model.Stock;

import java.util.Optional;
import java.util.UUID;

public interface FindStockByIdUseCase {
    Optional<Stock> findStockItemById(UUID id);
}
