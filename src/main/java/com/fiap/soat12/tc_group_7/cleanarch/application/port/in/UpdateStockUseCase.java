package com.fiap.soat12.tc_group_7.cleanarch.application.port.in;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;

import java.math.BigDecimal;
import java.util.UUID;

public interface UpdateStockUseCase {
    Stock updateStockItem(UpdateStockItemCommand command);

    record UpdateStockItemCommand(UUID id, String name, BigDecimal value, ToolCategory toolCategory) {}
}
