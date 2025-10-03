package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.application.port.in.FindStockByIdUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindStockByIdUseCaseImpl implements FindStockByIdUseCase {

    private final StockRepository stockRepository;

    @Override
    public Optional<Stock> findStockItemById(UUID id) {
        return stockRepository.findActiveById(id);
    }
}
