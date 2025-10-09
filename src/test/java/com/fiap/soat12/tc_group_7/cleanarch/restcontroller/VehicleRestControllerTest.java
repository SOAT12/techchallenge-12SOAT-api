package com.fiap.soat12.tc_group_7.cleanarch.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.cleanarch.controller.VehicleController;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VehicleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleController vehicleController;

    @Autowired
    private ObjectMapper objectMapper;

    private VehicleRequestDTO vehicleRequestDTO;
    private VehicleResponseDTO vehicleResponseDTO;

    @BeforeEach
    void setUp() {
        vehicleRequestDTO = new VehicleRequestDTO("RIO2A18", "Honda", "HRV", 2025, "Gray");
        vehicleResponseDTO = new VehicleResponseDTO(1L, "RIO2A18", "Honda", "HRV", 2025, "Gray", true);
    }

    @Test
    @DisplayName("Create Vehicle - Should return 201 Created")
    void createVehicle_Success() throws Exception {
        when(vehicleController.createVehicle(any(VehicleRequestDTO.class))).thenReturn(vehicleResponseDTO);

        mockMvc.perform(post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.licensePlate", is("RIO2A18")));

        verify(vehicleController, times(1)).createVehicle(any(VehicleRequestDTO.class));
    }

    @Test
    @DisplayName("Create Vehicle - Should return 400 Bad Request for invalid input")
    void createVehicle_BadRequest() throws Exception {
        when(vehicleController.createVehicle(any(VehicleRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));

        mockMvc.perform(post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequestDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Get Vehicle By ID - Should return 200 OK")
    void getVehicleById_Success() throws Exception {
        when(vehicleController.getVehicleById(1L)).thenReturn(vehicleResponseDTO);

        mockMvc.perform(get("/api/vehicle/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.brand", is("Honda")));

        verify(vehicleController, times(1)).getVehicleById(1L);
    }

    @Test
    @DisplayName("Get Vehicle By License Plate - Should return 200 OK")
    void getVehicleByLicensePlate_Success() throws Exception {
        when(vehicleController.getVehicleByLicensePlate("RIO2A18")).thenReturn(vehicleResponseDTO);

        mockMvc.perform(get("/api/vehicle/plate/{licensePlate}", "RIO2A18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licensePlate", is("RIO2A18")));
    }

    @Test
    @DisplayName("Get All Vehicles - Should return 200 OK with a list of vehicles")
    void getAllVehicles_Success() throws Exception {
        List<VehicleResponseDTO> vehicles = List.of(vehicleResponseDTO);
        when(vehicleController.getAllVehicles()).thenReturn(vehicles);

        mockMvc.perform(get("/api/vehicle/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @DisplayName("Get All Active Vehicles - Should return 200 OK with a list of active vehicles")
    void getAllVehiclesActive_Success() throws Exception {
        List<VehicleResponseDTO> activeVehicles = List.of(vehicleResponseDTO);
        when(vehicleController.getAllVehiclesActive()).thenReturn(activeVehicles);

        mockMvc.perform(get("/api/vehicle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].active", is(true)));
    }

    @Test
    @DisplayName("Update Vehicle - Should return 200 OK")
    void updateVehicle_Success() throws Exception {
        when(vehicleController.updateVehicle(anyLong(), any(VehicleRequestDTO.class))).thenReturn(vehicleResponseDTO);

        mockMvc.perform(put("/api/vehicle/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("Delete Vehicle - Should return 204 No Content")
    void deleteVehicle_Success() throws Exception {
        doNothing().when(vehicleController).deleteVehicle(1L);

        mockMvc.perform(delete("/api/vehicle/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(vehicleController, times(1)).deleteVehicle(1L);
    }

    @Test
    @DisplayName("Reactivate Vehicle - Should return 200 OK")
    void reactivateVehicle_Success() throws Exception {
        doNothing().when(vehicleController).reactivateVehicle(1L);

        mockMvc.perform(patch("/api/vehicle/{id}/reactivate", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

}
