package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.application.port.in.UpdateStockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateStockUseCaseImpl implements UpdateStockUseCase {

    private final StockRepository stockRepository;

    @Override
    public Stock updateStockItem(UpdateStockItemCommand command) {
        Stock stockItem = stockRepository.findActiveById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found with id: " + command.id()));

        stockItem.updateDetails(command.name(), command.value(), command.toolCategory());

        return stockRepository.save(stockItem);
    }
}
