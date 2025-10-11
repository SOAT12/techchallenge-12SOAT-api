package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleGatewayTest {

    private VehicleRepository vehicleRepository;
    private VehicleGateway gateway;

    @BeforeEach
    void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        gateway = new VehicleGateway(vehicleRepository);
    }

    @Test
    void findAll_shouldReturnListOfVehicles() {
        List<VehicleJpaEntity> jpaEntities = List.of(
                createVehicleJpaEntity(1L),
                createVehicleJpaEntity(2L)
        );

        when(vehicleRepository.findAll()).thenReturn(jpaEntities);

        List<Vehicle> vehicles = gateway.findAll();

        assertEquals(2, vehicles.size());
        verify(vehicleRepository).findAll();
    }

    @Test
    void findById_shouldReturnVehicle_whenFound() {
        VehicleJpaEntity jpaEntity = createVehicleJpaEntity(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(jpaEntity));

        Optional<Vehicle> result = gateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(jpaEntity.getId(), result.get().getId());
        verify(vehicleRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Vehicle> result = gateway.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByLicensePlate_shouldReturnVehicle_whenFound() {
        VehicleJpaEntity jpaEntity = createVehicleJpaEntity(1L);
        when(vehicleRepository.findByLicensePlate("ABC1234")).thenReturn(Optional.of(jpaEntity));

        Optional<Vehicle> result = gateway.findByLicensePlate("ABC1234");

        assertTrue(result.isPresent());
        assertEquals(jpaEntity.getLicensePlate(), result.get().getLicensePlate());
        verify(vehicleRepository).findByLicensePlate("ABC1234");
    }

    @Test
    void findByLicensePlate_shouldReturnEmpty_whenNotFound() {
        when(vehicleRepository.findByLicensePlate("XYZ0000")).thenReturn(Optional.empty());

        Optional<Vehicle> result = gateway.findByLicensePlate("XYZ0000");

        assertTrue(result.isEmpty());
    }

    @Test
    void save_shouldReturnSavedVehicle() {
        Vehicle vehicle = createVehicle(1L);
        VehicleJpaEntity jpaEntity = createVehicleJpaEntity(1L);

        when(vehicleRepository.save(any(VehicleJpaEntity.class))).thenReturn(jpaEntity);

        Vehicle saved = gateway.save(vehicle);

        assertNotNull(saved);
        assertEquals(vehicle.getLicensePlate(), saved.getLicensePlate());
        verify(vehicleRepository).save(any(VehicleJpaEntity.class));
    }

    private VehicleJpaEntity createVehicleJpaEntity(Long id) {
        return VehicleJpaEntity.builder()
                .id(id)
                .licensePlate("ABC1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .build();
    }

    private Vehicle createVehicle(Long id) {
        return Vehicle.builder()
                .id(id)
                .licensePlate("ABC1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Prata")
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

}
