package com.fiap.soat12.tc_group_7.api.application.port.in;

import com.fiap.soat12.tc_group_7.api.domain.model.Stock;
import com.fiap.soat12.tc_group_7.api.domain.model.ToolCategory;

import java.math.BigDecimal;
import java.util.UUID;

public interface UpdateStockUseCase {
    Stock updateStockItem(UpdateStockItemCommand command);

    record UpdateStockItemCommand(UUID id, String name, BigDecimal value, ToolCategory toolCategory) {}
}
