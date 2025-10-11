package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockControllerTest {

    @Mock
    private StockUseCase stockUseCase;

    @Mock
    private StockPresenter stockPresenter;

    @InjectMocks
    private StockController stockController;

    private ToolCategory validCategory;
    private UUID categoryId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validCategory = new ToolCategory(categoryId, "Test Category", true);
    }

    @Test
    void createStock_ShouldDelegateToUseCaseAndPresenter() {
        StockRequestDTO requestDTO = new StockRequestDTO("New Stock", BigDecimal.TEN, true, 10, categoryId);
        Stock domainObject = Stock.create("New Stock", BigDecimal.TEN, 10, validCategory);
        StockResponseDTO responseDTO = new StockResponseDTO();

        when(stockUseCase.createStock(anyString(), any(), anyInt(), any(UUID.class))).thenReturn(domainObject);
        when(stockPresenter.toStockResponseDTO(domainObject)).thenReturn(responseDTO);

        StockResponseDTO result = stockController.createStock(requestDTO);

        assertNotNull(result);
        verify(stockUseCase, times(1)).createStock("New Stock", BigDecimal.TEN, 10, categoryId);
        verify(stockPresenter, times(1)).toStockResponseDTO(domainObject);
    }

    @Test
    void getStockById_ShouldDelegateToUseCaseAndPresenter() {
        UUID id = UUID.randomUUID();
        Stock domainObject = new Stock(id, "Test", BigDecimal.ONE, 1, validCategory, true, null, null);
        StockResponseDTO responseDTO = new StockResponseDTO();

        when(stockUseCase.findStockItemById(id)).thenReturn(domainObject);
        when(stockPresenter.toStockResponseDTO(domainObject)).thenReturn(responseDTO);

        StockResponseDTO result = stockController.getStockById(id);

        assertNotNull(result);
        verify(stockUseCase, times(1)).findStockItemById(id);
        verify(stockPresenter, times(1)).toStockResponseDTO(domainObject);
    }

    @Test
    void getAllStockItems_ShouldReturnListOfDTOs() {
        Stock domainObject = Stock.create("Test", BigDecimal.ONE, 1, validCategory);
        List<Stock> domainList = Collections.singletonList(domainObject);
        StockResponseDTO responseDTO = new StockResponseDTO();

        when(stockUseCase.getAllStock()).thenReturn(domainList);
        when(stockPresenter.toStockResponseDTO(domainObject)).thenReturn(responseDTO);

        List<StockResponseDTO> result = stockController.getAllStockItems();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(stockUseCase, times(1)).getAllStock();
    }

    @Test
    void getAllStockItemsActive_ShouldReturnListOfDTOs() {
        Stock domainObject = Stock.create("Test", BigDecimal.ONE, 1, validCategory);
        List<Stock> domainList = Collections.singletonList(domainObject);
        StockResponseDTO responseDTO = new StockResponseDTO();

        when(stockUseCase.getAllActiveStockItems()).thenReturn(domainList);
        when(stockPresenter.toStockResponseDTO(domainObject)).thenReturn(responseDTO);

        List<StockResponseDTO> result = stockController.getAllStockItemsActive();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(stockUseCase, times(1)).getAllActiveStockItems();
    }

    @Test
    void updateStock_ShouldDelegateToUseCaseAndPresenter() {
        UUID id = UUID.randomUUID();
        StockRequestDTO requestDTO = new StockRequestDTO("Updated", BigDecimal.TEN, true, 20, categoryId);
        Stock domainObject = new Stock(id, "Updated", BigDecimal.TEN, 20, validCategory, true, null, null);
        StockResponseDTO responseDTO = new StockResponseDTO();

        when(stockUseCase.updateStockItem(id, "Updated", BigDecimal.TEN, 20, true, categoryId)).thenReturn(domainObject);
        when(stockPresenter.toStockResponseDTO(domainObject)).thenReturn(responseDTO);

        StockResponseDTO result = stockController.updateStock(id, requestDTO);

        assertNotNull(result);
        verify(stockUseCase, times(1)).updateStockItem(id, "Updated", BigDecimal.TEN, 20, true, categoryId);
        verify(stockPresenter, times(1)).toStockResponseDTO(domainObject);
    }

    @Test
    void reactivateStock_ShouldDelegateToUseCaseAndPresenter() {
        UUID id = UUID.randomUUID();
        Stock domainObject = new Stock(id, "Reactivated", BigDecimal.ONE, 1, validCategory, true, null, null);
        StockResponseDTO responseDTO = new StockResponseDTO();

        when(stockUseCase.reactivateStockItem(id)).thenReturn(domainObject);
        when(stockPresenter.toStockResponseDTO(domainObject)).thenReturn(responseDTO);

        StockResponseDTO result = stockController.reactivateStock(id);

        assertNotNull(result);
        verify(stockUseCase, times(1)).reactivateStockItem(id);
        verify(stockPresenter, times(1)).toStockResponseDTO(domainObject);
    }

    @Test
    void logicallyDeleteStock_ShouldCallUseCase() {
        UUID id = UUID.randomUUID();
        doNothing().when(stockUseCase).inactivateStockItem(id);

        stockController.logicallyDeleteStock(id);

        verify(stockUseCase, times(1)).inactivateStockItem(id);
    }
}
