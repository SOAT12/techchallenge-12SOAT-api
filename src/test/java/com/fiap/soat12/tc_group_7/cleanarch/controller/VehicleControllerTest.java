package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehiclePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleUseCase;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class VehicleControllerTest {

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
    void createVehicle_shouldReturnMappedDTO() {
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();
        Vehicle vehicle = mock(Vehicle.class);
        VehicleResponseDTO dto = new VehicleResponseDTO();

        when(vehicleUseCase.create(requestDTO)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(dto);

        VehicleResponseDTO result = controller.createVehicle(requestDTO);

        assertSame(dto, result);
        verify(vehicleUseCase).create(requestDTO);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getVehicleById_shouldReturnMappedDTO() {
        Long id = 1L;
        Vehicle vehicle = mock(Vehicle.class);
        VehicleResponseDTO dto = new VehicleResponseDTO();

        when(vehicleUseCase.getVehicleById(id)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(dto);

        VehicleResponseDTO result = controller.getVehicleById(id);

        assertSame(dto, result);
        verify(vehicleUseCase).getVehicleById(id);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getVehicleByLicensePlate_shouldReturnMappedDTO() {
        String licensePlate = "ABC-1234";
        Vehicle vehicle = mock(Vehicle.class);
        VehicleResponseDTO dto = new VehicleResponseDTO();

        when(vehicleUseCase.getVehicleByLicensePlate(licensePlate)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(dto);

        VehicleResponseDTO result = controller.getVehicleByLicensePlate(licensePlate);

        assertSame(dto, result);
        verify(vehicleUseCase).getVehicleByLicensePlate(licensePlate);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getAllVehicles_shouldReturnMappedDTOList() {
        Vehicle vehicle = mock(Vehicle.class);
        VehicleResponseDTO dto = new VehicleResponseDTO();
        List<Vehicle> vehicles = List.of(vehicle);

        when(vehicleUseCase.getAllVehicles()).thenReturn(vehicles);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(dto);

        List<VehicleResponseDTO> result = controller.getAllVehicles();

        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());
        verify(vehicleUseCase).getAllVehicles();
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void getAllVehiclesActive_shouldReturnMappedDTOList() {
        Vehicle vehicle = mock(Vehicle.class);
        VehicleResponseDTO dto = new VehicleResponseDTO();
        List<Vehicle> vehicles = List.of(vehicle);

        when(vehicleUseCase.getAllVehiclesActive()).thenReturn(vehicles);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(dto);

        List<VehicleResponseDTO> result = controller.getAllVehiclesActive();

        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());
        verify(vehicleUseCase).getAllVehiclesActive();
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void updateVehicle_shouldReturnMappedDTO() {
        Long id = 2L;
        VehicleRequestDTO requestDTO = new VehicleRequestDTO();
        Vehicle vehicle = mock(Vehicle.class);
        VehicleResponseDTO dto = new VehicleResponseDTO();

        when(vehicleUseCase.updateVehicle(id, requestDTO)).thenReturn(vehicle);
        when(vehiclePresenter.toVehicleResponseDTO(vehicle)).thenReturn(dto);

        VehicleResponseDTO result = controller.updateVehicle(id, requestDTO);

        assertSame(dto, result);
        verify(vehicleUseCase).updateVehicle(id, requestDTO);
        verify(vehiclePresenter).toVehicleResponseDTO(vehicle);
    }

    @Test
    void deleteVehicle_shouldCallUseCase() {
        Long id = 3L;

        controller.deleteVehicle(id);

        verify(vehicleUseCase).deleteVehicle(id);
        verifyNoMoreInteractions(vehiclePresenter);
    }

    @Test
    void reactivateVehicle_shouldCallUseCase() {
        Long id = 4L;

        controller.reactivateVehicle(id);

        verify(vehicleUseCase).reactivateVehicle(id);
        verifyNoMoreInteractions(vehiclePresenter);
    }

}
