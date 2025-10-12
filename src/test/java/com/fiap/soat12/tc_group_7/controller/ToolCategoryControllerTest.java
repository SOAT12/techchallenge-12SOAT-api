package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import com.fiap.soat12.tc_group_7.service.ToolCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de testes unitários para ToolCategoryController.
 * Utiliza @WebMvcTest para testar a camada web isoladamente.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ToolCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToolCategoryService toolCategoryService;

//    @Test
//    @DisplayName("POST /api/tool-categories - Deve criar uma nova categoria com sucesso")
//    void shouldCreateToolCategory() throws Exception {
//        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Ferramentas Manuais", true);
//        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO(1L, "Ferramentas Manuais", true);
//
//        when(toolCategoryService.createToolCategory(any(ToolCategoryRequestDTO.class))).thenReturn(responseDTO);
//
//        mockMvc.perform(post("/api/tool-categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.toolCategoryName").value("Ferramentas Manuais"))
//                .andExpect(jsonPath("$.active").value(true));
//    }
//
//    @Test
//    @DisplayName("POST /api/tool-categories - Deve retornar 400 Bad Request para nome de categoria em branco")
//    void shouldReturnBadRequestWhenCreatingToolCategoryWithBlankName() throws Exception {
//        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("", true);
//
//        mockMvc.perform(post("/api/tool-categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("GET /api/tool-categories/{id} - Deve retornar categoria pelo ID")
//    void shouldGetToolCategoryById() throws Exception {
//        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO(1L, "Pecas", true);
//
//        when(toolCategoryService.getToolCategoryById(1L)).thenReturn(Optional.of(responseDTO));
//
//        mockMvc.perform(get("/api/tool-categories/{id}", 1L)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.toolCategoryName").value("Pecas"))
//                .andExpect(jsonPath("$.active").value(true));
//    }
//
//    @Test
//    @DisplayName("GET /api/tool-categories/{id} - Deve retornar 404 Not Found se a categoria não for encontrada")
//    void shouldReturnNotFoundWhenToolCategoryNotFoundById() throws Exception {
//        when(toolCategoryService.getToolCategoryById(99L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/tool-categories/{id}", 99L)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("GET /api/tool-categories/all - Deve listar todas as categorias")
//    void shouldGetAllToolCategories() throws Exception {
//        List<ToolCategoryResponseDTO> responseList = Arrays.asList(
//                new ToolCategoryResponseDTO(1L, "Pecas", true),
//                new ToolCategoryResponseDTO(2L, "Insumos", true)
//        );
//
//        when(toolCategoryService.getAllToolCategories()).thenReturn(responseList);
//
//        mockMvc.perform(get("/api/tool-categories/all")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].toolCategoryName").value("Pecas"))
//                .andExpect(jsonPath("$[1].toolCategoryName").value("Insumos"))
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    @DisplayName("GET /api/tool-categories - Deve listar todas as categorias ativas")
//    void shouldGetAllToolCategoriesActive() throws Exception {
//        List<ToolCategoryResponseDTO> responseList = Arrays.asList(
//                new ToolCategoryResponseDTO(1L, "Pecas", true),
//                new ToolCategoryResponseDTO(2L, "Insumos", true)
//        );
//
//        when(toolCategoryService.getAllToolCategoriesActive()).thenReturn(responseList);
//
//        mockMvc.perform(get("/api/tool-categories")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].toolCategoryName").value("Pecas"))
//                .andExpect(jsonPath("$[1].toolCategoryName").value("Insumos"))
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    @DisplayName("PUT /api/tool-categories/{id} - Deve atualizar uma categoria com sucesso")
//    void shouldUpdateToolCategory() throws Exception {
//        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Ferramentas de Medição", false);
//        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO(1L, "Ferramentas de Medição", false);
//
//        when(toolCategoryService.updateToolCategory(eq(1L), any(ToolCategoryRequestDTO.class))).thenReturn(Optional.of(responseDTO));
//
//        mockMvc.perform(put("/api/tool-categories/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.toolCategoryName").value("Ferramentas de Medição"))
//                .andExpect(jsonPath("$.active").value(false));
//    }

//    @Test
//    @DisplayName("PUT /api/tool-categories/{id} - Deve retornar 404 Not Found se a categoria a ser atualizada não for encontrada")
//    void shouldReturnNotFoundWhenUpdatingNonExistentToolCategory() throws Exception {
//        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Categoria Inexistente", true);
//
//        when(toolCategoryService.updateToolCategory(eq(99L), any(ToolCategoryRequestDTO.class))).thenReturn(Optional.empty());
//
//        mockMvc.perform(put("/api/tool-categories/{id}", 99L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("DELETE /api/tool-categories/{id} - Deve deletar logicamente uma categoria com sucesso")
//    void shouldLogicallyDeleteToolCategory() throws Exception {
//        when(toolCategoryService.logicallyDeleteToolCategory(1L)).thenReturn(true);
//
//        mockMvc.perform(delete("/api/tool-categories/{id}", 1L))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @DisplayName("DELETE /api/tool-categories/{id} - Deve retornar 404 Not Found se a categoria a ser deletada não for encontrada")
//    void shouldReturnNotFoundWhenLogicallyDeletingNonExistentToolCategory() throws Exception {
//        when(toolCategoryService.logicallyDeleteToolCategory(99L)).thenReturn(false);
//
//        mockMvc.perform(delete("/api/tool-categories/{id}", 99L))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("PATCH /api/tool-categories/{id}/reactivate - Deve reativar uma categoria com sucesso")
//    void shouldReactivateToolCategory() throws Exception {
//        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO(2L, "Insumos", true);
//
//        when(toolCategoryService.reactivateToolCategory(2L)).thenReturn(Optional.of(responseDTO));
//
//        mockMvc.perform(patch("/api/tool-categories/{id}/reactivate", 2L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(2L))
//                .andExpect(jsonPath("$.toolCategoryName").value("Insumos"))
//                .andExpect(jsonPath("$.active").value(true));
//    }
//
//    @Test
//    @DisplayName("PATCH /api/tool-categories/{id}/reactivate - Deve retornar 404 Not Found se a categoria a ser reativada não for encontrada")
//    void shouldReturnNotFoundWhenReactivatingNonExistentToolCategory() throws Exception {
//        when(toolCategoryService.reactivateToolCategory(99L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(patch("/api/tool-categories/{id}/reactivate", 99L))
//                .andExpect(status().isNotFound());
//    }
}
