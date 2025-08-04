package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import com.fiap.soat12.tc_group_7.service.VehicleServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleServiceController.class)
public class VehicleServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleServiceService vehicleServiceService;

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

        when(vehicleServiceService.getAllActiveVehicleServices()).thenReturn(list);

        // Act & Assert
        mockMvc.perform(get("/v1/vehicle-services")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Troca de óleo"))
                .andExpect(jsonPath("$[1].name").value("Balanceamento"));

        verify(vehicleServiceService).getAllActiveVehicleServices();
    }

    @Test
    void testCreateVehicleService_Returns201() throws Exception {
        VehicleServiceRequestDTO request = VehicleServiceRequestDTO.builder()
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100))
                .build();

        VehicleServiceResponseDTO response = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100))
                .build();

        when(vehicleServiceService.create(any())).thenReturn(response);

        mockMvc.perform(post("/vehicle-services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Alinhamento",
                                  "value": 100.00
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alinhamento"))
                .andExpect(jsonPath("$.value").value(100.00))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testUpdateVehicleService_Returns200() throws Exception {
        Long id = 1L;
        VehicleServiceResponseDTO response = VehicleServiceResponseDTO.builder()
                .id(id)
                .name("Balanceamento")
                .value(BigDecimal.valueOf(90))
                .build();

        when(vehicleServiceService.update(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/vehicle-services/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Balanceamento",
                                  "value": 90.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Balanceamento"))
                .andExpect(jsonPath("$.value").value(90.00));
    }

    @Test
    void testDeactivateVehicleService_Returns204() throws Exception {
        Long id = 1L;

        doNothing().when(vehicleServiceService).deactivate(id);

        mockMvc.perform(patch("/vehicle-services/{id}/deactivate", id))
                .andExpect(status().isNoContent());

        verify(vehicleServiceService).deactivate(id);
    }

}
