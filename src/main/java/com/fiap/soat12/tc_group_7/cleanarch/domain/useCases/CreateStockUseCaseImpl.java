package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.application.port.in.CreateStockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateStockUseCaseImpl implements CreateStockUseCase {

    private final StockRepository stockRepository;

    public Stock createStock(CreateStockCommand command) {
        if (stockRepository.findByName(command.toolName()).isPresent()) {

            throw new IllegalStateException("A stock item with the name '" +
                    command.toolName() + "' already exists.");
        }

        var newStock = new Stock(
                command.toolName(),
                command.value(),
                command.quantity(),
                command.toolCategory());

        return stockRepository.save(newStock);
    }
}
