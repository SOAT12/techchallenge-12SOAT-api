package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import com.fiap.soat12.tc_group_7.config.SessionToken;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockApi.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class StockApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockController stockController;

    @MockitoBean
    private SessionToken sessionToken;

    private StockRequestDTO getValidStockRequestDTO() {
        return new StockRequestDTO("Hammer", BigDecimal.TEN, true, 10, UUID.randomUUID());
    }

    private StockResponseDTO getValidStockResponseDTO() {
        return StockResponseDTO.builder()
                .id(UUID.randomUUID())
                .toolName("Hammer")
                .isActive(true)
                .quantity(10)
                .value(BigDecimal.TEN)
                .build();
    }

    @Nested
    class CreateStock {
        @Test
        void createStock_ShouldReturn201() throws Exception {
            StockRequestDTO requestDTO = getValidStockRequestDTO();
            StockResponseDTO responseDTO = getValidStockResponseDTO();

            given(stockController.createStock(any(StockRequestDTO.class))).willReturn(responseDTO);

            mockMvc.perform(post("/clean-arch/stock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.toolName").value(responseDTO.getToolName()));
        }

        @Test
        void createStock_WhenControllerThrowsError_ShouldReturn400() throws Exception {
            StockRequestDTO requestDTO = getValidStockRequestDTO();
            given(stockController.createStock(any(StockRequestDTO.class)))
                    .willThrow(new IllegalArgumentException("Invalid data"));

            mockMvc.perform(post("/clean-arch/stock")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetStock {
        @Test
        void getStockById_ShouldReturn200() throws Exception {
            UUID id = UUID.randomUUID();
            StockResponseDTO responseDTO = getValidStockResponseDTO();
            responseDTO.setId(id);

            given(stockController.getStockById(id)).willReturn(responseDTO);

            mockMvc.perform(get("/clean-arch/stock/id").param("id", id.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()));
        }

        @Test
        void getAllStockItems_ShouldReturn200() throws Exception {
            StockResponseDTO responseDTO = getValidStockResponseDTO();
            given(stockController.getAllStockItems()).willReturn(Collections.singletonList(responseDTO));

            mockMvc.perform(get("/clean-arch/stock/all"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].toolName").value(responseDTO.getToolName()));
        }
         @Test
        void getAllStockItemsActive_ShouldReturn200() throws Exception {
            StockResponseDTO responseDTO = getValidStockResponseDTO();
            given(stockController.getAllStockItemsActive()).willReturn(Collections.singletonList(responseDTO));

            mockMvc.perform(get("/clean-arch/stock"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].toolName").value(responseDTO.getToolName()));
        }
    }

    @Nested
    class UpdateStock {
        @Test
        void updateStock_ShouldReturn200() throws Exception {
            UUID id = UUID.randomUUID();
            StockRequestDTO requestDTO = getValidStockRequestDTO();
            StockResponseDTO responseDTO = getValidStockResponseDTO();
            responseDTO.setId(id);

            given(stockController.updateStock(any(UUID.class), any(StockRequestDTO.class))).willReturn(responseDTO);

            mockMvc.perform(put("/clean-arch/stock/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()));
        }
    }

    @Nested
    class DeleteStock {
        @Test
        void deleteStock_ShouldReturn204() throws Exception {
            UUID id = UUID.randomUUID();
            mockMvc.perform(delete("/clean-arch/stock/{id}", id))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    class ReactivateStock {
        @Test
        void reactivateStock_ShouldReturn200() throws Exception {
            UUID id = UUID.randomUUID();
            StockResponseDTO responseDTO = getValidStockResponseDTO();
            responseDTO.setId(id);

            given(stockController.reactivateStock(id)).willReturn(responseDTO);

            mockMvc.perform(patch("/clean-arch/stock/{id}/reactivate", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isActive").value(true));
        }
    }
}