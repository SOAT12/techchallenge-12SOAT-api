package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockGatewayTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockGateway stockGateway;

    private ToolCategory validCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validCategory = new ToolCategory(UUID.randomUUID(), "Test Category", true);
    }

    @Test
    void findByName_ShouldCallRepository() {
        String name = "Test Stock";
        Stock stock = Stock.create(name, BigDecimal.ONE, 1, validCategory);
        when(stockRepository.findByName(name)).thenReturn(Optional.of(stock));

        Optional<Stock> result = stockGateway.findByName(name);

        assertTrue(result.isPresent());
        assertEquals(name, result.get().getToolName());
        verify(stockRepository, times(1)).findByName(name);
    }

    @Test
    void save_ShouldCallRepository() {
        Stock stock = Stock.create("New Stock", BigDecimal.ONE, 1, validCategory);
        when(stockRepository.save(stock)).thenReturn(stock);

        Stock result = stockGateway.save(stock);

        assertNotNull(result);
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    void findActiveById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();
        Stock stock = new Stock(id, "Test", BigDecimal.ONE, 1, validCategory, true, null, null);
        when(stockRepository.findActiveById(id)).thenReturn(Optional.of(stock));

        Optional<Stock> result = stockGateway.findActiveById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(stockRepository, times(1)).findActiveById(id);
    }

    @Test
    void findAll_ShouldCallRepository() {
        List<Stock> stocks = Collections.singletonList(Stock.create("Test", BigDecimal.ONE, 1, validCategory));
        when(stockRepository.findAll()).thenReturn(stocks);

        List<Stock> result = stockGateway.findAll();

        assertFalse(result.isEmpty());
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void findAllActive_ShouldCallRepository() {
        List<Stock> stocks = Collections.singletonList(Stock.create("Test", BigDecimal.ONE, 1, validCategory));
        when(stockRepository.findAllActive()).thenReturn(stocks);

        List<Stock> result = stockGateway.findAllActive();

        assertFalse(result.isEmpty());
        verify(stockRepository, times(1)).findAllActive();
    }

    @Test
    void findById_ShouldCallRepository() {
        UUID id = UUID.randomUUID();
        Stock stock = new Stock(id, "Test", BigDecimal.ONE, 1, validCategory, true, null, null);
        when(stockRepository.findById(id)).thenReturn(Optional.of(stock));

        Optional<Stock> result = stockGateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(stockRepository, times(1)).findById(id);
    }
}
