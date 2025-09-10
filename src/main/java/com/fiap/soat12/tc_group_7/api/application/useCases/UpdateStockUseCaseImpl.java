package com.fiap.soat12.tc_group_7.api.application.useCases;

import com.fiap.soat12.tc_group_7.api.application.port.in.UpdateStockUseCase;
import com.fiap.soat12.tc_group_7.api.application.port.out.StockRepositoryPort;
import com.fiap.soat12.tc_group_7.api.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateStockUseCaseImpl implements UpdateStockUseCase {

    private final StockRepositoryPort stockRepositoryPort;

    @Override
    public Stock updateStockItem(UpdateStockItemCommand command) {
        // 1. Find the existing item
        Stock stockItem = stockRepositoryPort.findActiveById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found with id: " + command.id()));

        // 2. Use the domain model to perform the update
        stockItem.updateDetails(command.name(), command.value(), command.toolCategory());

        // 3. Save the updated item
        return stockRepositoryPort.save(stockItem);
    }
}
