package com.fiap.soat12.tc_group_7.cleanarch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.cleanarch.controller.VehicleServiceController;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VehicleServiceApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleServiceController vehicleServiceController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllActiveVehicleServices_withSuccess() throws Exception {
        // Arrange
        VehicleServiceResponseDTO dto1 = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(150))
                .build();
        VehicleServiceResponseDTO dto2 = VehicleServiceResponseDTO.builder()
                .id(2L)
                .name("Balanceamento")
                .value(BigDecimal.valueOf(80))
                .build();
        List<VehicleServiceResponseDTO> list = List.of(dto1, dto2);

        when(vehicleServiceController.getAllActiveVehicleServices()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/clean-arch/vehicle-services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Troca de óleo"))
                .andExpect(jsonPath("$[1].name").value("Balanceamento"));

        verify(vehicleServiceController).getAllActiveVehicleServices();
    }

    @Test
    void getAllVehicleServices_withSuccess() throws Exception {
        // Arrange
        VehicleServiceResponseDTO dto1 = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(150))
                .build();
        VehicleServiceResponseDTO dto2 = VehicleServiceResponseDTO.builder()
                .id(2L)
                .name("Balanceamento")
                .value(BigDecimal.valueOf(80))
                .build();
        List<VehicleServiceResponseDTO> list = List.of(dto1, dto2);

        when(vehicleServiceController.getAllVehicleServices()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/clean-arch/vehicle-services/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Troca de óleo"))
                .andExpect(jsonPath("$[1].name").value("Balanceamento"));

        verify(vehicleServiceController).getAllVehicleServices();
    }

    @Test
    void getById_withSuccess() throws Exception {
        // Arrange
        Long id = 1L;
        VehicleServiceResponseDTO responseDTO = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(150))
                .build();

        when(vehicleServiceController.getById(id)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/clean-arch/vehicle-services/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Troca de óleo"))
                .andExpect(jsonPath("$.value").value(150));

        verify(vehicleServiceController).getById(id);
    }

    @Test
    void create_withSuccess() throws Exception {
        VehicleServiceRequestDTO request = VehicleServiceRequestDTO.builder()
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100.00))
                .build();

        VehicleServiceResponseDTO response = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100.00))
                .build();

        when(vehicleServiceController.create(any())).thenReturn(response);

        mockMvc.perform(post("/clean-arch/vehicle-services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alinhamento"))
                .andExpect(jsonPath("$.value").value(100.00));
    }

    @Test
    void update_withSuccess() throws Exception {
        Long id = 1L;
        VehicleServiceRequestDTO request = VehicleServiceRequestDTO.builder()
                .name("Balanceamento")
                .value(BigDecimal.valueOf(90.00))
                .build();
        VehicleServiceResponseDTO response = VehicleServiceResponseDTO.builder()
                .id(id)
                .name("Balanceamento")
                .value(BigDecimal.valueOf(90.00))
                .build();

        when(vehicleServiceController.update(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/clean-arch/vehicle-services/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Balanceamento"))
                .andExpect(jsonPath("$.value").value(90.00));
    }

    @Test
    void deactivate_withSuccess() throws Exception {
        Long id = 1L;

        doNothing().when(vehicleServiceController).deactivate(id);

        mockMvc.perform(delete("/clean-arch/vehicle-services/{id}", id))
                .andExpect(status().isOk());

        verify(vehicleServiceController).deactivate(id);
    }

    @Test
    void activate_withSuccess() throws Exception {
        Long id = 1L;

        doNothing().when(vehicleServiceController).activate(id);

        mockMvc.perform(put("/clean-arch/vehicle-services/{id}/activate", id))
                .andExpect(status().isOk());

        verify(vehicleServiceController).activate(id);
    }

}
