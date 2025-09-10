package com.fiap.soat12.tc_group_7.api.application.port.in;

import java.util.UUID;

public interface InactivateStockUseCase {
    void inactivateStockItem(UUID id);
}
