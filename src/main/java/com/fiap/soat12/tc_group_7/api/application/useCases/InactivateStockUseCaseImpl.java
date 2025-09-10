package com.fiap.soat12.tc_group_7.api.application.useCases;

import com.fiap.soat12.tc_group_7.api.application.port.in.InactivateStockUseCase;
import com.fiap.soat12.tc_group_7.api.application.port.out.StockRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InactivateStockUseCaseImpl implements InactivateStockUseCase {

    private final StockRepositoryPort stockRepositoryPort;

    @Override
    public void inactivateStockItem(UUID id) {
        stockRepositoryPort.inactivateById(id);
    }
}
