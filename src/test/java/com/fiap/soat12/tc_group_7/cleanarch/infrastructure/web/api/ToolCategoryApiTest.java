package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ToolCategoryController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ToolCategoryApi.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class ToolCategoryApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ToolCategoryController toolCategoryController;

    @Test
    void createToolCategory_ShouldReturn201() throws Exception {
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Hammers");
        ToolCategoryResponseDTO responseDTO = ToolCategoryResponseDTO.builder().id(UUID.randomUUID()).toolCategoryName("Hammers").active(true).build();

        given(toolCategoryController.createToolCategory(any(ToolCategoryRequestDTO.class))).willReturn(responseDTO);

        mockMvc.perform(post("/clean-arch/tool-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.toolCategoryName").value("Hammers"));
    }

    @Test
    void getToolCategoryById_ShouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        ToolCategoryResponseDTO responseDTO = ToolCategoryResponseDTO.builder().id(id).toolCategoryName("Hammers").active(true).build();

        given(toolCategoryController.getToolCategoryById(id)).willReturn(responseDTO);

        mockMvc.perform(get("/clean-arch/tool-categories/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getAllToolCategories_ShouldReturn200() throws Exception {
        ToolCategoryResponseDTO responseDTO = ToolCategoryResponseDTO.builder().id(UUID.randomUUID()).toolCategoryName("Hammers").active(true).build();
        given(toolCategoryController.getAllToolCategories()).willReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/clean-arch/tool-categories/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].toolCategoryName").value("Hammers"));
    }

    @Test
    void updateToolCategory_ShouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Updated Hammers");
        ToolCategoryResponseDTO responseDTO = ToolCategoryResponseDTO.builder().id(id).toolCategoryName("Updated Hammers").active(true).build();

        given(toolCategoryController.updateToolCategory(any(UUID.class), any(ToolCategoryRequestDTO.class))).willReturn(responseDTO);

        mockMvc.perform(put("/clean-arch/tool-categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.toolCategoryName").value("Updated Hammers"));
    }

    @Test
    void deleteToolCategory_ShouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/clean-arch/tool-categories/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void reactivateToolCategory_ShouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        ToolCategoryResponseDTO responseDTO = ToolCategoryResponseDTO.builder().id(id).toolCategoryName("Hammers").active(true).build();

        given(toolCategoryController.reactivateToolCategory(id)).willReturn(responseDTO);

        mockMvc.perform(patch("/clean-arch/tool-categories/{id}/reactivate", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void createToolCategory_WhenControllerThrowsError_ShouldReturn400() throws Exception {
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO("Error Case");
        given(toolCategoryController.createToolCategory(any(ToolCategoryRequestDTO.class)))
                .willThrow(new IllegalArgumentException("Invalid name"));

        mockMvc.perform(post("/clean-arch/tool-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}
