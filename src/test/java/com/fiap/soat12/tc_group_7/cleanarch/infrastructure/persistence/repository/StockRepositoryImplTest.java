package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.StockEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.StockMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockRepositoryImplTest {

    @Mock
    private StockJpaRepository stockJpaRepository;

    private StockRepositoryImpl stockRepository;

    private ToolCategoryEntity toolCategoryEntity;
    private ToolCategory toolCategoryDomain;
    private UUID categoryId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockRepository = new StockRepositoryImpl(stockJpaRepository);

        toolCategoryEntity = ToolCategoryEntity.builder().id(categoryId).toolCategoryName("Test").active(true).build();
        toolCategoryDomain = new ToolCategory(categoryId, "Test", true);
    }

    private StockEntity createStockEntity(UUID id, String name, boolean isActive) {
        return StockEntity.builder()
                .id(id)
                .toolName(name)
                .active(isActive)
                .toolCategoryEntity(toolCategoryEntity)
                .quantity(10)
                .value(BigDecimal.TEN)
                .build();
    }

    @Test
    void save_ShouldMapAndCallJpaSave() {
        UUID id = UUID.randomUUID();
        Stock domain = new Stock(id, "Test Save", BigDecimal.ONE, 1, toolCategoryDomain, true, null, null);
        StockEntity entity = new StockMapper().toEntity(domain);

        when(stockJpaRepository.save(any(StockEntity.class))).thenReturn(entity);

        Stock result = stockRepository.save(domain);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(stockJpaRepository, times(1)).save(any(StockEntity.class));
    }

    @Test
    void findById_ShouldReturnDomainObject() {
        UUID id = UUID.randomUUID();
        StockEntity entity = createStockEntity(id, "Test Find", true);
        when(stockJpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<Stock> result = stockRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(stockJpaRepository, times(1)).findById(id);
    }

    @Test
    void findActiveById_ShouldReturnDomainObject() {
        UUID id = UUID.randomUUID();
        StockEntity entity = createStockEntity(id, "Test Find Active", true);
        when(stockJpaRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(entity));

        Optional<Stock> result = stockRepository.findActiveById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(stockJpaRepository, times(1)).findByIdAndActiveTrue(id);
    }

    @Test
    void findByName_ShouldReturnDomainObject() {
        String name = "Test Find By Name";
        StockEntity entity = createStockEntity(UUID.randomUUID(), name, true);
        when(stockJpaRepository.findByToolName(name)).thenReturn(Optional.of(entity));

        Optional<Stock> result = stockRepository.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getToolName());
        verify(stockJpaRepository, times(1)).findByToolName(name);
    }

    @Test
    void findAllActive_ShouldReturnListOfDomainObjects() {
        StockEntity entity = createStockEntity(UUID.randomUUID(), "Test Active", true);
        when(stockJpaRepository.findByActiveTrue()).thenReturn(Collections.singletonList(entity));

        List<Stock> result = stockRepository.findAllActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(stockJpaRepository, times(1)).findByActiveTrue();
    }

    @Test
    void findAll_ShouldReturnListOfDomainObjects() {
        StockEntity entity = createStockEntity(UUID.randomUUID(), "Test All", true);
        when(stockJpaRepository.findAll()).thenReturn(Collections.singletonList(entity));

        List<Stock> result = stockRepository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(stockJpaRepository, times(1)).findAll();
    }
}
