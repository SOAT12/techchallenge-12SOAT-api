package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.application.port.in.InactivateStockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InactivateStockUseCaseImpl implements InactivateStockUseCase {

    private final StockRepository stockRepository;

    @Override
    public void inactivateStockItem(UUID id) {
        stockRepository.inactivateById(id);
    }
}
