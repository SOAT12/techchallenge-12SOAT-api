package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VehicleGatewayTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleGateway vehicleGateway;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
    }

    @Test
    void findAll_shouldReturnListOfVehicles() {
        List<Vehicle> vehicles = List.of(vehicle);

        when(vehicleRepository.findAll()).thenReturn(vehicles);

        List<Vehicle> result = vehicleGateway.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicle, result.getFirst());
        verify(vehicleRepository).findAll();
    }

    @Test
    void findById_shouldReturnVehicleIfExists() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = vehicleGateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
        verify(vehicleRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(vehicleRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleGateway.findById(2L);

        assertFalse(result.isPresent());
        verify(vehicleRepository).findById(2L);
    }

    @Test
    void findByLicensePlate_shouldReturnVehicleIfExists() {
        when(vehicleRepository.findByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> result = vehicleGateway.findByLicensePlate("ABC1234");

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
        verify(vehicleRepository).findByLicensePlate("ABC1234");
    }

    @Test
    void findByLicensePlate_shouldReturnEmptyIfNotFound() {
        when(vehicleRepository.findByLicensePlate("XYZ9999")).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleGateway.findByLicensePlate("XYZ9999");

        assertFalse(result.isPresent());
        verify(vehicleRepository).findByLicensePlate("XYZ9999");
    }

    @Test
    void save_shouldReturnSavedVehicle() {
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle result = vehicleGateway.save(vehicle);

        assertNotNull(result);
        assertEquals(vehicle, result);
        verify(vehicleRepository).save(vehicle);
    }

}
