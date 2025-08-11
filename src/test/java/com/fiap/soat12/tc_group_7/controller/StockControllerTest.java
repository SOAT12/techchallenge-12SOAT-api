package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.stock.StockRequestDTO;
import com.fiap.soat12.tc_group_7.dto.stock.StockResponseDTO;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import com.fiap.soat12.tc_group_7.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de testes unitários para StockController.
 * Utiliza @WebMvcTest para testar a camada web isoladamente.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockService stockService;

    private final ToolCategoryResponseDTO categoryResponseDTO = new ToolCategoryResponseDTO(1L, "Pecas", true);

    @Test
    @DisplayName("POST /api/stock - Deve criar um novo item de estoque com sucesso")
    void shouldCreateStockItem() throws Exception {
        StockRequestDTO requestDTO = new StockRequestDTO("Chave de Fenda", new BigDecimal("15.50"), true, 100, 1L);
        StockResponseDTO responseDTO = new StockResponseDTO(101L, "Chave de Fenda", new BigDecimal("15.50"), true, 100, new Date(), new Date(), categoryResponseDTO);

        when(stockService.createStock(any(StockRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.toolName").value("Chave de Fenda"))
                .andExpect(jsonPath("$.toolCategory.toolCategoryName").value("Pecas"));
    }

    @Test
    @DisplayName("POST /api/stock - Deve retornar 400 Bad Request se a categoria não for encontrada")
    void shouldReturnBadRequestWhenCategoryNotFoundOnCreate() throws Exception {
        StockRequestDTO requestDTO = new StockRequestDTO("Chave de Fenda", new BigDecimal("15.50"), true, 100, 99L);

        when(stockService.createStock(any(StockRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Categoria da ferramenta não encontrada com ID: 99"));

        mockMvc.perform(post("/api/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Categoria da ferramenta não encontrada com ID: 99"));
    }

    @Test
    @DisplayName("GET /api/stock/{id} - Deve retornar item de estoque pelo ID")
    void shouldGetStockItemById() throws Exception {
        StockResponseDTO responseDTO = new StockResponseDTO(101L, "Chave de Fenda", new BigDecimal("15.50"), true, 100, new Date(), new Date(), categoryResponseDTO);

        when(stockService.getStockById(101L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/stock/{id}", 101L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.toolName").value("Chave de Fenda"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("GET /api/stock/{id} - Deve retornar 404 Not Found se o item de estoque não for encontrado ou estiver inativo")
    void shouldReturnNotFoundWhenStockItemNotFoundById() throws Exception {
        when(stockService.getStockById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/stock/{id}", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/stock/all - Deve listar todos os itens de estoque")
    void shouldGetAllStockItems() throws Exception {
        List<StockResponseDTO> responseList = Arrays.asList(
                new StockResponseDTO(101L, "Chave de Fenda", new BigDecimal("15.50"), true, 100, new Date(), new Date(), categoryResponseDTO),
                new StockResponseDTO(102L, "Parafuso", new BigDecimal("0.75"), true, 5000, new Date(), new Date(), categoryResponseDTO)
        );

        when(stockService.getAllStockItems()).thenReturn(responseList);

        mockMvc.perform(get("/api/stock/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].toolName").value("Chave de Fenda"))
                .andExpect(jsonPath("$[1].toolName").value("Parafuso"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/stock - Deve listar todos os itens de estoque ativos")
    void shouldGetAllStockItemsActive() throws Exception {
        List<StockResponseDTO> responseList = Arrays.asList(
                new StockResponseDTO(101L, "Chave de Fenda", new BigDecimal("15.50"), true, 100, new Date(), new Date(), categoryResponseDTO),
                new StockResponseDTO(102L, "Parafuso", new BigDecimal("0.75"), true, 5000, new Date(), new Date(), categoryResponseDTO)
        );

        when(stockService.getAllStockItemsActive()).thenReturn(responseList);

        mockMvc.perform(get("/api/stock")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].toolName").value("Chave de Fenda"))
                .andExpect(jsonPath("$[1].toolName").value("Parafuso"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT /api/stock/{id} - Deve atualizar um item de estoque com sucesso")
    void shouldUpdateStockItem() throws Exception {
        StockRequestDTO requestDTO = new StockRequestDTO("Martelo de Borracha", new BigDecimal("35.00"), false, 60, 1L);
        StockResponseDTO responseDTO = new StockResponseDTO(101L, "Martelo de Borracha", new BigDecimal("35.00"), false, 60, new Date(), new Date(), categoryResponseDTO);

        when(stockService.updateStock(eq(101L), any(StockRequestDTO.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/stock/{id}", 101L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.toolName").value("Martelo de Borracha"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    @DisplayName("PUT /api/stock/{id} - Deve retornar 404 Not Found se o item de estoque a ser atualizado não for encontrado")
    void shouldReturnNotFoundWhenUpdatingNonExistentStockItem() throws Exception {
        StockRequestDTO requestDTO = new StockRequestDTO("Item Inexistente", new BigDecimal("10.00"), true, 10, 1L);

        when(stockService.updateStock(eq(999L), any(StockRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/stock/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/stock/{id} - Deve retornar 400 Bad Request se a categoria da ferramenta não for encontrada ao atualizar")
    void shouldReturnBadRequestWhenCategoryNotFoundOnUpdate() throws Exception {
        StockRequestDTO requestDTO = new StockRequestDTO("Martelo de Borracha", new BigDecimal("35.00"), false, 60, 99L);

        when(stockService.updateStock(eq(101L), any(StockRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Categoria da ferramenta não encontrada com ID: 99"));

        mockMvc.perform(put("/api/stock/{id}", 101L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Categoria da ferramenta não encontrada com ID: 99"));
    }

    @Test
    @DisplayName("DELETE /api/stock/{id} - Deve deletar logicamente um item de estoque com sucesso")
    void shouldLogicallyDeleteStockItem() throws Exception {
        when(stockService.logicallyDeleteStock(101L)).thenReturn(true);

        mockMvc.perform(delete("/api/stock/{id}", 101L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/stock/{id} - Deve retornar 404 Not Found se o item de estoque a ser deletado não for encontrado")
    void shouldReturnNotFoundWhenLogicallyDeletingNonExistentStockItem() throws Exception {
        when(stockService.logicallyDeleteStock(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/stock/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/stock/{id}/reactivate - Deve reativar um item de estoque com sucesso")
    void shouldReactivateStockItem() throws Exception {
        StockResponseDTO responseDTO = new StockResponseDTO(102L, "Parafuso", new BigDecimal("0.75"), true, 5000, new Date(), new Date(), categoryResponseDTO);

        when(stockService.reactivateStock(102L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(patch("/api/stock/{id}/reactivate", 102L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(102L))
                .andExpect(jsonPath("$.toolName").value("Parafuso"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("PATCH /api/stock/{id}/reactivate - Deve retornar 404 Not Found se o item de estoque a ser reativado não for encontrado")
    void shouldReturnNotFoundWhenReactivatingNonExistentStockItem() throws Exception {
        when(stockService.reactivateStock(999L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/stock/{id}/reactivate", 999L))
                .andExpect(status().isNotFound());
    }
}
