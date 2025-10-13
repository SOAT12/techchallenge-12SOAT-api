package com.fiap.soat12.tc_group_7.cleanarch.usercase;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.VehicleUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleGateway;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleUseCaseTest {

    private VehicleGateway vehicleGateway;
    private VehicleUseCase vehicleUseCase;

    @BeforeEach
    void setUp() {
        vehicleGateway = mock(VehicleGateway.class);
        vehicleUseCase = new VehicleUseCase(vehicleGateway);
    }

    @Test
    void createVehicle_shouldReturnSavedVehicle() {
        VehicleRequestDTO request = createRequestDTO();
        Vehicle expected = createVehicle();

        when(vehicleGateway.save(any(Vehicle.class))).thenReturn(expected);

        Vehicle result = vehicleUseCase.create(request);

        assertNotNull(result);
        assertEquals(expected, result);
        verify(vehicleGateway).save(any(Vehicle.class));
    }

    @Test
    void getVehicleById_shouldReturnVehicle_whenFound() {
        Vehicle vehicle = createVehicle();
        when(vehicleGateway.findById(1L)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleUseCase.getVehicleById(1L);

        assertEquals(vehicle, result);
    }

    @Test
    void getVehicleById_shouldThrowException_whenNotFound() {
        when(vehicleGateway.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> vehicleUseCase.getVehicleById(1L));

        assertEquals(VehicleUseCase.VEHICLE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void getVehicleByLicensePlate_shouldReturnVehicle_whenFound() {
        Vehicle vehicle = createVehicle();
        when(vehicleGateway.findByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleUseCase.getVehicleByLicensePlate("ABC1234");

        assertEquals(vehicle, result);
    }

    @Test
    void getVehicleByLicensePlate_shouldThrowException_whenNotFound() {
        when(vehicleGateway.findByLicensePlate("ABC1234")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> vehicleUseCase.getVehicleByLicensePlate("ABC1234"));

        assertEquals(VehicleUseCase.VEHICLE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void getAllVehicles_shouldReturnList() {
        List<Vehicle> vehicles = List.of(createVehicle(), createVehicle());
        when(vehicleGateway.findAll()).thenReturn(vehicles);

        List<Vehicle> result = vehicleUseCase.getAllVehicles();

        assertEquals(2, result.size());
        verify(vehicleGateway).findAll();
    }

    @Test
    void getAllVehiclesActive_shouldReturnOnlyActiveVehicles() {
        Vehicle active = createVehicle();
        Vehicle inactive = createVehicle();
        inactive.setActive(false);

        when(vehicleGateway.findAll()).thenReturn(List.of(active, inactive));

        List<Vehicle> result = vehicleUseCase.getAllVehiclesActive();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().getActive());
    }

    @Test
    void updateVehicle_shouldUpdateAndReturnVehicle_whenFound() {
        Vehicle existing = createVehicle();
        VehicleRequestDTO request = createRequestDTO();

        when(vehicleGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(vehicleGateway.save(any(Vehicle.class))).thenReturn(existing);

        Vehicle result = vehicleUseCase.updateVehicle(1L, request);

        assertEquals(existing, result);
        verify(vehicleGateway).save(existing);
    }

    @Test
    void updateVehicle_shouldThrowException_whenNotFound() {
        when(vehicleGateway.findById(1L)).thenReturn(Optional.empty());

        VehicleRequestDTO request = createRequestDTO();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> vehicleUseCase.updateVehicle(1L, request));

        assertEquals(VehicleUseCase.VEHICLE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void deleteVehicle_shouldSetInactive_whenFound() {
        Vehicle vehicle = createVehicle();
        when(vehicleGateway.findById(1L)).thenReturn(Optional.of(vehicle));

        vehicleUseCase.deleteVehicle(1L);

        assertFalse(vehicle.getActive());
        verify(vehicleGateway).save(vehicle);
    }

    @Test
    void deleteVehicle_shouldThrowException_whenNotFound() {
        when(vehicleGateway.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> vehicleUseCase.deleteVehicle(1L));

        assertEquals(VehicleUseCase.VEHICLE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void reactivateVehicle_shouldSetActiveTrue_whenFound() {
        Vehicle vehicle = createVehicle();
        vehicle.setActive(false);
        when(vehicleGateway.findById(1L)).thenReturn(Optional.of(vehicle));

        vehicleUseCase.reactivateVehicle(1L);

        assertTrue(vehicle.getActive());
        verify(vehicleGateway).save(vehicle);
    }

    @Test
    void reactivateVehicle_shouldThrowException_whenNotFound() {
        when(vehicleGateway.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> vehicleUseCase.reactivateVehicle(1L));

        assertEquals(VehicleUseCase.VEHICLE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    private Vehicle createVehicle() {
        return Vehicle.builder()
                .id(1L)
                .licensePlate("ABC1234")
                .brand("Ford")
                .model("Fiesta")
                .year(2020)
                .color("Preto")
                .active(true)
                .build();
    }

    private VehicleRequestDTO createRequestDTO() {
        VehicleRequestDTO dto = new VehicleRequestDTO();
        dto.setLicensePlate("ABC1234");
        dto.setBrand("Ford");
        dto.setModel("Fiesta");
        dto.setYear(2020);
        dto.setColor("Preto");
        return dto;
    }

}
