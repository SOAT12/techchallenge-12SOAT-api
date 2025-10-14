package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockApiTest {

    @Mock
    private StockController stockController;

    private StockApi stockApi;

    private final UUID sampleUuid = UUID.randomUUID();

    @BeforeEach
    void setup() {
        stockApi = new StockApi(stockController);
    }

    @Test
    void createStock_ShouldReturnCreatedStock() {
        StockRequestDTO requestDTO = new StockRequestDTO();
        // preencher campos obrigatórios do requestDTO

        StockResponseDTO responseDTO = StockResponseDTO.builder()
                .id(sampleUuid)
                .build();

        when(stockController.createStock(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<StockResponseDTO> response = stockApi.createStock(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleUuid, response.getBody().getId());
        verify(stockController).createStock(requestDTO);
    }

    @Test
    void createStock_ShouldThrowBadRequest_WhenInvalidData() {
        StockRequestDTO invalidRequest = new StockRequestDTO();
        // dados inválidos

        when(stockController.createStock(invalidRequest)).thenThrow(new IllegalArgumentException("Categoria não encontrada"));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            stockApi.createStock(invalidRequest);
        });

        assertEquals("Categoria não encontrada", thrown.getReason());
        verify(stockController).createStock(invalidRequest);
    }

    @Test
    void getStockById_ShouldReturnStock() {
        StockResponseDTO responseDTO = StockResponseDTO.builder()
                .id(sampleUuid)
                .build();

        when(stockController.getStockById(sampleUuid)).thenReturn(responseDTO);

        StockResponseDTO result = stockApi.getStockById(sampleUuid);

        assertEquals(sampleUuid, result.getId());
        verify(stockController).getStockById(sampleUuid);
    }

    @Test
    void getAllStockItemsActive_ShouldReturnList() {
        List<StockResponseDTO> list = List.of(
                StockResponseDTO.builder().id(UUID.randomUUID()).build(),
                StockResponseDTO.builder().id(UUID.randomUUID()).build()
        );

        when(stockController.getAllStockItemsActive()).thenReturn(list);

        List<StockResponseDTO> result = stockApi.getAllStockItemsActive();

        assertEquals(2, result.size());
        verify(stockController).getAllStockItemsActive();
    }

    @Test
    void updateStock_ShouldReturnUpdatedStock() {
        StockRequestDTO requestDTO = new StockRequestDTO();

        StockResponseDTO responseDTO = StockResponseDTO.builder()
                .id(sampleUuid)
                .build();

        when(stockController.updateStock(sampleUuid, requestDTO)).thenReturn(responseDTO);

        StockResponseDTO result = stockApi.updateStock(sampleUuid, requestDTO);

        assertEquals(sampleUuid, result.getId());
        verify(stockController).updateStock(sampleUuid, requestDTO);
    }

    @Test
    void deleteStock_ShouldCallDelete() {
        doNothing().when(stockController).logicallyDeleteStock(sampleUuid);

        stockApi.deleteStock(sampleUuid);

        verify(stockController).logicallyDeleteStock(sampleUuid);
    }

    @Test
    void reactivateStock_ShouldReturnStock() {
        StockResponseDTO responseDTO = StockResponseDTO.builder()
                .id(sampleUuid)
                .build();

        when(stockController.reactivateStock(sampleUuid)).thenReturn(responseDTO);

        StockResponseDTO result = stockApi.reactivateStock(sampleUuid);

        assertEquals(sampleUuid, result.getId());
        verify(stockController).reactivateStock(sampleUuid);
    }

}
