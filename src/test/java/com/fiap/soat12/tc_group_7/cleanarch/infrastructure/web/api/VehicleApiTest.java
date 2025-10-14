package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.VehicleController;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleApiTest {

    @Mock
    private VehicleController vehicleController;

    private VehicleApi vehicleApi;

    private final Long sampleId = 1L;

    @BeforeEach
    void setup() {
        vehicleApi = new VehicleApi(vehicleController);
    }

    @Test
    void createVehicle_ShouldReturnCreatedVehicle() {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();

        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleController.createVehicle(requestDTO)).thenReturn(responseDTO);

        VehicleResponseDTO result = vehicleApi.createVehicle(requestDTO);

        assertEquals(responseDTO, result);
        verify(vehicleController).createVehicle(requestDTO);
    }

    @Test
    void createVehicle_ShouldThrowBadRequest_WhenInvalid() {
        VehicleRequestDTO invalidRequest = new VehicleRequestDTO();

        when(vehicleController.createVehicle(invalidRequest))
                .thenThrow(new IllegalArgumentException("Requisição inválida"));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            vehicleApi.createVehicle(invalidRequest);
        });

        assertEquals("Requisição inválida", thrown.getReason());
        verify(vehicleController).createVehicle(invalidRequest);
    }

    @Test
    void getVehicleById_ShouldReturnVehicle() {
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleController.getVehicleById(sampleId)).thenReturn(responseDTO);

        VehicleResponseDTO result = vehicleApi.getVehicleById(sampleId);

        assertEquals(responseDTO, result);
        verify(vehicleController).getVehicleById(sampleId);
    }

    @Test
    void getVehicleByLicensePlate_ShouldReturnVehicle() {
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        String samplePlate = "ABC1234";
        when(vehicleController.getVehicleByLicensePlate(samplePlate)).thenReturn(responseDTO);

        VehicleResponseDTO result = vehicleApi.getVehicleByLicensePlate(samplePlate);

        assertEquals(responseDTO, result);
        verify(vehicleController).getVehicleByLicensePlate(samplePlate);
    }

    @Test
    void getAllVehicles_ShouldReturnList() {
        List<VehicleResponseDTO> list = List.of(new VehicleResponseDTO(), new VehicleResponseDTO());

        when(vehicleController.getAllVehicles()).thenReturn(list);

        List<VehicleResponseDTO> result = vehicleApi.getAllVehicles();

        assertEquals(2, result.size());
        verify(vehicleController).getAllVehicles();
    }

    @Test
    void getAllVehiclesActive_ShouldReturnList() {
        List<VehicleResponseDTO> list = List.of(new VehicleResponseDTO(), new VehicleResponseDTO());

        when(vehicleController.getAllVehiclesActive()).thenReturn(list);

        List<VehicleResponseDTO> result = vehicleApi.getAllVehiclesActive();

        assertEquals(2, result.size());
        verify(vehicleController).getAllVehiclesActive();
    }

    @Test
    void updateVehicle_ShouldReturnUpdatedVehicle() {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();

        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleController.updateVehicle(sampleId, requestDTO)).thenReturn(responseDTO);

        VehicleResponseDTO result = vehicleApi.updateVehicle(sampleId, requestDTO);

        assertEquals(responseDTO, result);
        verify(vehicleController).updateVehicle(sampleId, requestDTO);
    }

    @Test
    void deleteVehicle_ShouldCallDelete() {
        doNothing().when(vehicleController).deleteVehicle(sampleId);

        vehicleApi.deleteVehicle(sampleId);

        verify(vehicleController).deleteVehicle(sampleId);
    }

    @Test
    void reactivateVehicle_ShouldCallReactivate() {
        doNothing().when(vehicleController).reactivateVehicle(sampleId);

        vehicleApi.reactivateVehicle(sampleId);

        verify(vehicleController).reactivateVehicle(sampleId);
    }

}
