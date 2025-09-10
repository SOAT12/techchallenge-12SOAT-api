package com.fiap.soat12.tc_group_7.api.application.useCases;

import com.fiap.soat12.tc_group_7.api.application.port.in.FindStockByIdUseCase;
import com.fiap.soat12.tc_group_7.api.application.port.out.StockRepositoryPort;
import com.fiap.soat12.tc_group_7.api.domain.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindStockByIdUseCaseImpl implements FindStockByIdUseCase {

    private final StockRepositoryPort stockRepositoryPort;

    @Override
    public Optional<Stock> findStockItemById(UUID id) {
        return stockRepositoryPort.findActiveById(id);
    }
}
