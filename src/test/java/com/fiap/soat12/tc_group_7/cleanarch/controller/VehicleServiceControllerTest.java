package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.VehicleUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.VehicleController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.VehiclePresenter;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class VehicleServiceControllerTest {

    private VehicleUseCase vehicleUseCase;
    private VehiclePresenter vehiclePresenter;
    private VehicleController controller;

    @BeforeEach
    void setup() {
        vehicleUseCase = mock(VehicleUseCase.class);
        vehiclePresenter = mock(VehiclePresenter.class);
        controller = new VehicleController(vehicleUseCase, vehiclePresenter);
    }

    @Test
    void createVehicle_shouldReturnVehicleResponseDTO() {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleUseCase.create(requestDTO)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(responseDTO);

        VehicleResponseDTO result = controller.createVehicle(requestDTO);

        assertEquals(responseDTO, result);
        verify(vehicleUseCase).create(requestDTO);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getVehicleById_shouldReturnVehicleResponseDTO() {
        Long id = 1L;
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleUseCase.getVehicleById(id)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(responseDTO);

        VehicleResponseDTO result = controller.getVehicleById(id);

        assertEquals(responseDTO, result);
        verify(vehicleUseCase).getVehicleById(id);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getVehicleByLicensePlate_shouldReturnVehicleResponseDTO() {
        String licensePlate = "ABC-1234";
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleUseCase.getVehicleByLicensePlate(licensePlate)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(responseDTO);

        VehicleResponseDTO result = controller.getVehicleByLicensePlate(licensePlate);

        assertEquals(responseDTO, result);
        verify(vehicleUseCase).getVehicleByLicensePlate(licensePlate);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getAllVehicles_shouldReturnListOfVehicleResponseDTO() {
        Vehicle vehicle1 = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
        Vehicle vehicle2 = Vehicle.builder()
                .id(1L)
                .licensePlate("DEF-5678")
                .brand("Toyota")
                .model("Camry")
                .year(2022)
                .color("Branco")
                .active(true)
                .build();
        List<Vehicle> vehicles = Arrays.asList(vehicle1, vehicle2);

        VehicleResponseDTO dto1 = new VehicleResponseDTO();
        VehicleResponseDTO dto2 = new VehicleResponseDTO();

        when(vehicleUseCase.getAllVehicles()).thenReturn(vehicles);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle1)).thenReturn(dto1);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle2)).thenReturn(dto2);

        List<VehicleResponseDTO> result = controller.getAllVehicles();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        verify(vehicleUseCase).getAllVehicles();
        verify(vehiclePresenter, times(2)).toVehicleResponseDTO(any());
    }

    @Test
    void getAllVehiclesActive_shouldReturnListOfVehicleResponseDTO() {
        Vehicle vehicle1 = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
        Vehicle vehicle2 = Vehicle.builder()
                .id(1L)
                .licensePlate("DEF-5678")
                .brand("Toyota")
                .model("Camry")
                .year(2022)
                .color("Branco")
                .active(true)
                .build();
        List<Vehicle> vehicles = Arrays.asList(vehicle1, vehicle2);

        VehicleResponseDTO dto1 = new VehicleResponseDTO();
        VehicleResponseDTO dto2 = new VehicleResponseDTO();

        when(vehicleUseCase.getAllVehiclesActive()).thenReturn(vehicles);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle1)).thenReturn(dto1);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle2)).thenReturn(dto2);

        List<VehicleResponseDTO> result = controller.getAllVehiclesActive();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        verify(vehicleUseCase).getAllVehiclesActive();
        verify(vehiclePresenter, times(2)).toVehicleResponseDTO(any());
    }

    @Test
    void updateVehicle_shouldReturnVehicleResponseDTO() {
        Long id = 1L;
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("DEF-5678")
                .brand("Toyota")
                .model("Camry")
                .year(2022)
                .color("Branco")
                .active(true)
                .build();
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();

        when(vehicleUseCase.updateVehicle(id, requestDTO)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(responseDTO);

        VehicleResponseDTO result = controller.updateVehicle(id, requestDTO);

        assertEquals(responseDTO, result);
        verify(vehicleUseCase).updateVehicle(id, requestDTO);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void deleteVehicle_shouldCallUseCase() {
        Long id = 1L;

        doNothing().when(vehicleUseCase).deleteVehicle(id);

        controller.deleteVehicle(id);

        verify(vehicleUseCase).deleteVehicle(id);
    }

    @Test
    void reactivateVehicle_shouldCallUseCase() {
        Long id = 1L;

        doNothing().when(vehicleUseCase).reactivateVehicle(id);

        controller.reactivateVehicle(id);

        verify(vehicleUseCase).reactivateVehicle(id);
    }

}
