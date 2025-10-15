package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.exception.StockUnavailableException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.soat12.tc_group_7.dto.stock.StockAvailabilityResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @Mock
    private ToolCategoryGateway toolCategoryGateway;

    @InjectMocks
    private StockUseCase stockUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CreateStock {
        @Test
        void shouldCreateStock() {
            // Arrange
            UUID toolCategoryId = UUID.randomUUID();
            ToolCategory toolCategory = new ToolCategory(toolCategoryId, "Category", true);
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.of(toolCategory));
            when(stockGateway.findByName(anyString())).thenReturn(Optional.empty());
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Stock result = stockUseCase.createStock("Tool", BigDecimal.TEN, 10, toolCategoryId);

            // Assert
            assertNotNull(result);
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenCategoryNotFound() {
            // Arrange
            UUID toolCategoryId = UUID.randomUUID();
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> stockUseCase.createStock("Tool", BigDecimal.TEN, 10, toolCategoryId));
            verify(stockGateway, never()).save(any(Stock.class));
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenItemAlreadyExists() {
            // Arrange
            UUID toolCategoryId = UUID.randomUUID();
            ToolCategory toolCategory = new ToolCategory(toolCategoryId, "Category", true);
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.of(toolCategory));
            when(stockGateway.findByName(anyString())).thenReturn(Optional.of(Stock.builder().build()));

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> stockUseCase.createStock("Tool", BigDecimal.TEN, 10, toolCategoryId));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class UpdateStockItem {
        @Test
        void shouldUpdateStockItem() {
            // Arrange
            UUID id = UUID.randomUUID();
            UUID toolCategoryId = UUID.randomUUID();
            ToolCategory toolCategory = new ToolCategory(toolCategoryId, "Category", true);
            Stock existingStock = Stock.builder().id(id).toolName("Old Tool").quantity(5).build();

            when(stockGateway.findById(id)).thenReturn(Optional.of(existingStock));
            when(toolCategoryGateway.findById(toolCategoryId)).thenReturn(Optional.of(toolCategory));
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Stock result = stockUseCase.updateStockItem(id, "New Tool", BigDecimal.ONE, 10, true, toolCategoryId);

            // Assert
            assertNotNull(result);
            assertEquals("New Tool", result.getToolName());
            assertEquals(10, result.getQuantity());
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.updateStockItem(id, "New Tool", BigDecimal.ONE, 10, true, UUID.randomUUID()));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class GetAllStock {
        @Test
        void shouldGetAllStock() {
            // Arrange
            when(stockGateway.findAll()).thenReturn(List.of(Stock.builder().build()));

            // Act
            List<Stock> result = stockUseCase.getAllStock();

            // Assert
            assertFalse(result.isEmpty());
            verify(stockGateway).findAll();
        }
    }

    @Nested
    class GetAllActiveStockItems {
        @Test
        void shouldGetAllActiveStockItems() {
            // Arrange
            when(stockGateway.findAllActive()).thenReturn(List.of(Stock.builder().build()));

            // Act
            List<Stock> result = stockUseCase.getAllActiveStockItems();

            // Assert
            assertFalse(result.isEmpty());
            verify(stockGateway).findAllActive();
        }
    }

    @Nested
    class FindStockItemById {
        @Test
        void shouldFindStockItemById() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findActiveById(id)).thenReturn(Optional.of(Stock.builder().build()));

            // Act
            Stock result = stockUseCase.findStockItemById(id);

            // Assert
            assertNotNull(result);
            verify(stockGateway).findActiveById(id);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findActiveById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.findStockItemById(id));
        }
    }

    @Nested
    class InactivateStockItem {
        @Test
        void shouldInactivateStockItem() {
            // Arrange
            UUID id = UUID.randomUUID();
            Stock stock = Stock.builder().id(id).isActive(true).build();
            when(stockGateway.findById(id)).thenReturn(Optional.of(stock));
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            stockUseCase.inactivateStockItem(id);

            // Assert
            assertFalse(stock.isActive());
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.inactivateStockItem(id));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class ReactivateStockItem {
        @Test
        void shouldReactivateStockItem() {
            // Arrange
            UUID id = UUID.randomUUID();
            Stock stock = Stock.builder().id(id).isActive(false).build();
            when(stockGateway.findById(id)).thenReturn(Optional.of(stock));
            when(stockGateway.save(any(Stock.class))).thenAnswer(i -> i.getArguments()[0]);

            // Act
            Stock result = stockUseCase.reactivateStockItem(id);

            // Assert
            assertTrue(result.isActive());
            verify(stockGateway).save(any(Stock.class));
        }

        @Test
        void shouldThrowNotFoundExceptionWhenStockItemNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(stockGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> stockUseCase.reactivateStockItem(id));
            verify(stockGateway, never()).save(any(Stock.class));
        }
    }

    @Nested
    class CheckStockAvailability {
        @Test
        void shouldNotThrowExceptionWhenStockIsAvailable() {
            // Arrange
            UUID stockId = UUID.randomUUID();
            Stock requiredItem = Stock.builder().id(stockId).quantity(5).build();
            Stock availableStock = Stock.builder().id(stockId).quantity(10).build();
            ServiceOrder order = ServiceOrder.builder().stockItems(Set.of(requiredItem)).build();

            when(stockGateway.findActiveById(stockId)).thenReturn(Optional.of(availableStock));

            // Act & Assert
            assertDoesNotThrow(() -> stockUseCase.checkStockAvailability(order));
        }

        @Test
        void shouldThrowStockUnavailableExceptionWhenStockIsNotAvailable() {
            // Arrange
            UUID stockId = UUID.randomUUID();
            Stock requiredItem = Stock.builder().id(stockId).quantity(15).build();
            Stock availableStock = Stock.builder().id(stockId).quantity(10).build();
            ServiceOrder order = ServiceOrder.builder().stockItems(Set.of(requiredItem)).build();

            when(stockGateway.findActiveById(stockId)).thenReturn(Optional.of(availableStock));

            // Act & Assert
            assertThrows(StockUnavailableException.class, () -> stockUseCase.checkStockAvailability(order));
        }
    }

    @Nested
    class GetStockAvailability {
        @Test
        void shouldReturnAllItemsAvailableWhenStockIsAvailable() {
            // Arrange
            UUID stockId = UUID.randomUUID();
            Stock requiredItem = Stock.builder().id(stockId).quantity(5).build();
            Stock availableStock = Stock.builder().id(stockId).toolName("Tool").quantity(10).build();
            ServiceOrder order = ServiceOrder.builder().stockItems(Set.of(requiredItem)).build();

            when(stockGateway.findActiveById(stockId)).thenReturn(Optional.of(availableStock));

            // Act
            StockAvailabilityResponseDTO result = stockUseCase.getStockAvailability(order);

            // Assert
            assertTrue(result.isAvailable());
            assertTrue(result.getMissingItems().isEmpty());
        }

        @Test
        void shouldReturnMissingItemsWhenStockIsNotAvailable() {
            // Arrange
            UUID stockId = UUID.randomUUID();
            Stock requiredItem = Stock.builder().id(stockId).quantity(15).build();
            Stock availableStock = Stock.builder().id(stockId).toolName("Tool").quantity(10).build();
            ServiceOrder order = ServiceOrder.builder().stockItems(Set.of(requiredItem)).build();

            when(stockGateway.findActiveById(stockId)).thenReturn(Optional.of(availableStock));

            // Act
            StockAvailabilityResponseDTO result = stockUseCase.getStockAvailability(order);

            // Assert
            assertFalse(result.isAvailable());
            assertFalse(result.getMissingItems().isEmpty());
            assertEquals(1, result.getMissingItems().size());
            assertEquals(stockId, result.getMissingItems().get(0).getStockId());
        }
    }
}