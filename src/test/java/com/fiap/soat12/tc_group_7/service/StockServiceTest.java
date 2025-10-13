package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.stock.StockRequestDTO;
import com.fiap.soat12.tc_group_7.dto.stock.StockResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Stock;
import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import com.fiap.soat12.tc_group_7.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes unitários para StockService.
 * Utiliza Mockito para simular o comportamento do StockRepository e ToolCategoryService.
 */
@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ToolCategoryService toolCategoryService;

    @InjectMocks
    private StockService stockService;

    private ToolCategory categoryPecas;
    private Stock activeStockItem;
    private Stock inactiveStockItem;
    private StockRequestDTO stockRequestDTO;

    @BeforeEach
    void setUp() {
        categoryPecas = new ToolCategory(1L, "Pecas", true);
        activeStockItem = new Stock(UUID.randomUUID(), "Chave de Fenda", new BigDecimal("15.50"), true, 100, categoryPecas);
        inactiveStockItem = new Stock(UUID.randomUUID(), "Parafuso", new BigDecimal("0.75"), false, 5000, categoryPecas);
        stockRequestDTO = new StockRequestDTO("Martelo", new BigDecimal("30.00"), true, 50, 1L);
    }

    @Test
    @DisplayName("Deve criar um novo item de estoque com sucesso")
    void createStock_Success() {
        when(toolCategoryService.getToolCategoryEntityById(1L)).thenReturn(Optional.of(categoryPecas));
        when(stockRepository.save(any(Stock.class))).thenReturn(activeStockItem);

        StockResponseDTO result = stockService.createStock(stockRequestDTO);

        assertNotNull(result);
        assertEquals(activeStockItem.getId(), result.getId());
        assertEquals(activeStockItem.getToolName(), result.getToolName());
        assertEquals(activeStockItem.getToolCategory().getToolCategoryName(), result.getToolCategory().getToolCategoryName());

        verify(toolCategoryService, times(1)).getToolCategoryEntityById(1L);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar estoque com categoria inexistente")
    void createStock_IllegalArgumentException() {
        when(toolCategoryService.getToolCategoryEntityById(99L)).thenReturn(Optional.empty());
        stockRequestDTO.setToolCategoryId(99L);

        assertThrows(IllegalArgumentException.class, () -> stockService.createStock(stockRequestDTO));

        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    @DisplayName("Deve retornar um item de estoque ativo pelo ID")
    void getStockById_Success() {
        when(stockRepository.findById(101L)).thenReturn(Optional.of(activeStockItem));

        Optional<StockResponseDTO> result = stockService.getStockById(101L);

        assertTrue(result.isPresent());
        assertEquals(activeStockItem.getId(), result.get().getId());
        assertEquals(activeStockItem.getToolName(), result.get().getToolName());
        assertTrue(result.get().getActive());

        verify(stockRepository, times(1)).findById(101L);
    }

    @Test
    @DisplayName("Não deve retornar um item de estoque inativo pelo ID")
    void getStockById_NotActive() {
        when(stockRepository.findById(102L)).thenReturn(Optional.of(inactiveStockItem));

        Optional<StockResponseDTO> result = stockService.getStockById(102L);

        assertFalse(result.isPresent());

        verify(stockRepository, times(1)).findById(102L);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty se o item de estoque não for encontrado pelo ID")
    void getStockById_NotFound() {
        when(stockRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<StockResponseDTO> result = stockService.getStockById(999L);

        assertFalse(result.isPresent());

        verify(stockRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve listar todos os itens de estoque")
    void getAllStockItems_Success() {
        when(stockRepository.findAll()).thenReturn(Arrays.asList(activeStockItem, inactiveStockItem));

        List<StockResponseDTO> result = stockService.getAllStockItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(activeStockItem.getToolName(), result.getFirst().getToolName());
        assertTrue(result.getFirst().getActive());

        verify(stockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar todos os itens de estoque ativos")
    void getAllStockItemsActive_Success() {
        when(stockRepository.findByActiveTrue()).thenReturn(Arrays.asList(activeStockItem));

        List<StockResponseDTO> result = stockService.getAllStockItemsActive();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activeStockItem.getToolName(), result.getFirst().getToolName());
        assertTrue(result.getFirst().getActive());

        verify(stockRepository, times(1)).findByActiveTrue();
    }

    @Test
    @DisplayName("Deve atualizar um item de estoque existente com sucesso")
    void updateStock_Success() {
        StockRequestDTO updateRequest = new StockRequestDTO(
                "Martelo de Borracha", new BigDecimal("35.00"), false, 60, 1L
        );
        Stock updatedStockItem = new Stock(UUID.randomUUID(), "Martelo de Borracha", new BigDecimal("35.00"), false, 60, categoryPecas);

        when(stockRepository.findById(101L)).thenReturn(Optional.of(activeStockItem));
        when(toolCategoryService.getToolCategoryEntityById(1L)).thenReturn(Optional.of(categoryPecas));
        when(stockRepository.save(any(Stock.class))).thenReturn(updatedStockItem);

        Optional<StockResponseDTO> result = stockService.updateStock(101L, updateRequest);

        assertTrue(result.isPresent());
        assertEquals(updatedStockItem.getId(), result.get().getId());
        assertEquals(updatedStockItem.getToolName(), result.get().getToolName());
        assertEquals(updatedStockItem.getValue(), result.get().getValue());
        assertEquals(updatedStockItem.getActive(), result.get().getActive());
        assertEquals(updatedStockItem.getQuantity(), result.get().getQuantity());

        verify(stockRepository, times(1)).findById(101L);
        verify(toolCategoryService, times(1)).getToolCategoryEntityById(1L);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    @DisplayName("Não deve atualizar item de estoque se não for encontrado")
    void updateStock_NotFoundItem() {
        when(stockRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<StockResponseDTO> result = stockService.updateStock(999L, stockRequestDTO);

        assertFalse(result.isPresent());

        verify(stockRepository, times(1)).findById(999L);
        verify(toolCategoryService, never()).getToolCategoryEntityById(anyLong());
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar estoque com categoria inexistente")
    void updateStock_NotFoundToolCategory() {
        when(stockRepository.findById(101L)).thenReturn(Optional.of(activeStockItem));
        when(toolCategoryService.getToolCategoryEntityById(99L)).thenReturn(Optional.empty());

        stockRequestDTO.setToolCategoryId(99L);

        assertThrows(IllegalArgumentException.class, () -> stockService.updateStock(101L, stockRequestDTO));

        verify(stockRepository, times(1)).findById(101L);
        verify(toolCategoryService, times(1)).getToolCategoryEntityById(99L);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    @DisplayName("Deve deletar logicamente um item de estoque com sucesso")
    void logicallyDeleteStock_Success() {
        when(stockRepository.findById(101L)).thenReturn(Optional.of(activeStockItem));
        when(stockRepository.save(any(Stock.class))).thenReturn(activeStockItem);

        boolean result = stockService.logicallyDeleteStock(101L);

        assertTrue(result);
        assertFalse(activeStockItem.getActive());

        verify(stockRepository, times(1)).findById(101L);
        verify(stockRepository, times(1)).save(activeStockItem);
    }

    @Test
    @DisplayName("Não deve deletar logicamente um item de estoque se não for encontrado")
    void logicallyDeleteStock_NotFound() {
        when(stockRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = stockService.logicallyDeleteStock(999L);

        assertFalse(result);

        verify(stockRepository, times(1)).findById(999L);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    @DisplayName("Deve reativar um item de estoque com sucesso")
    void reactivateStock_Success() {
        when(stockRepository.findById(102L)).thenReturn(Optional.of(inactiveStockItem));
        when(stockRepository.save(any(Stock.class))).thenReturn(inactiveStockItem);

        Optional<StockResponseDTO> result = stockService.reactivateStock(102L);

        assertTrue(result.isPresent());
        assertTrue(inactiveStockItem.getActive());

        verify(stockRepository, times(1)).findById(102L);
        verify(stockRepository, times(1)).save(inactiveStockItem);
    }

    @Test
    @DisplayName("Não deve reativar item de estoque se não for encontrado")
    void reactivateStock_NotFound() {
        when(stockRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<StockResponseDTO> result = stockService.reactivateStock(999L);

        assertFalse(result.isPresent());

        verify(stockRepository, times(1)).findById(999L);
        verify(stockRepository, never()).save(any(Stock.class));
    }
}
