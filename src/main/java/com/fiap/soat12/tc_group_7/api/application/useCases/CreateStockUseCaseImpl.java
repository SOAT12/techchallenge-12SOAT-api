package com.fiap.soat12.tc_group_7.api.application.useCases;

import com.fiap.soat12.tc_group_7.api.application.port.in.CreateStockUseCase;
import com.fiap.soat12.tc_group_7.api.application.port.out.StockRepositoryPort;
import com.fiap.soat12.tc_group_7.api.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateStockUseCaseImpl implements CreateStockUseCase {

    private final StockRepositoryPort stockRepositoryPort;

    public Stock createStock(CreateStockCommand command) {
        if (stockRepositoryPort.findByName(command.toolName()).isPresent()) {

            throw new IllegalStateException("A stock item with the name '" +
                    command.toolName() + "' already exists.");
        }

        var newStock = new Stock(
                command.toolName(),
                command.value(),
                command.quantity(),
                command.toolCategory());

        return stockRepositoryPort.save(newStock);
    }
}
