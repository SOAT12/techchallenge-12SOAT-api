package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.VehicleJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleRepositoryImplTest {

    @Mock
    private VehicleJpaRepository vehicleJpaRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleRepositoryImpl vehicleRepositoryImpl;

    private Vehicle vehicle;
    private VehicleJpaEntity vehicleJpaEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC1234")
                .brand("Fiat")
                .model("Palio")
                .year(2019)
                .color("Preto")
                .active(true)
                .build();

        vehicleJpaEntity = VehicleJpaEntity.builder()
                .id(1L)
                .licensePlate("ABC1234")
                .brand("Fiat")
                .model("Palio")
                .year(2019)
                .color("Preto")
                .active(true)
                .build();
    }

    @Test
    void findAll_shouldReturnListOfVehicles() {
        when(vehicleJpaRepository.findAll()).thenReturn(List.of(vehicleJpaEntity));
        when(vehicleMapper.toVehicle(vehicleJpaEntity)).thenReturn(vehicle);

        List<Vehicle> result = vehicleRepositoryImpl.findAll();

        assertEquals(1, result.size());
        assertEquals(vehicle, result.get(0));
        verify(vehicleJpaRepository).findAll();
        verify(vehicleMapper).toVehicle(vehicleJpaEntity);
    }

    @Test
    void findById_shouldReturnVehicleIfExists() {
        when(vehicleJpaRepository.findById(1L)).thenReturn(Optional.of(vehicleJpaEntity));
        when(vehicleMapper.toVehicle(vehicleJpaEntity)).thenReturn(vehicle);

        Optional<Vehicle> result = vehicleRepositoryImpl.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
        verify(vehicleJpaRepository).findById(1L);
        verify(vehicleMapper).toVehicle(vehicleJpaEntity);
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(vehicleJpaRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleRepositoryImpl.findById(2L);

        assertFalse(result.isPresent());
        verify(vehicleJpaRepository).findById(2L);
        verify(vehicleMapper, never()).toVehicle(any());
    }

    @Test
    void findByLicensePlate_shouldReturnVehicleIfExists() {
        when(vehicleJpaRepository.findByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicleJpaEntity));
        when(vehicleMapper.toVehicle(vehicleJpaEntity)).thenReturn(vehicle);

        Optional<Vehicle> result = vehicleRepositoryImpl.findByLicensePlate("ABC1234");

        assertTrue(result.isPresent());
        assertEquals(vehicle, result.get());
        verify(vehicleJpaRepository).findByLicensePlate("ABC1234");
        verify(vehicleMapper).toVehicle(vehicleJpaEntity);
    }

    @Test
    void findByLicensePlate_shouldReturnEmptyIfNotFound() {
        when(vehicleJpaRepository.findByLicensePlate("XYZ9999")).thenReturn(Optional.empty());

        Optional<Vehicle> result = vehicleRepositoryImpl.findByLicensePlate("XYZ9999");

        assertFalse(result.isPresent());
        verify(vehicleJpaRepository).findByLicensePlate("XYZ9999");
        verify(vehicleMapper, never()).toVehicle(any());
    }

    @Test
    void save_shouldPersistVehicleAndReturnMappedResult() {
        when(vehicleMapper.toVehicleJpaEntity(vehicle)).thenReturn(vehicleJpaEntity);
        when(vehicleJpaRepository.save(vehicleJpaEntity)).thenReturn(vehicleJpaEntity);
        when(vehicleMapper.toVehicle(vehicleJpaEntity)).thenReturn(vehicle);

        Vehicle result = vehicleRepositoryImpl.save(vehicle);

        assertNotNull(result);
        assertEquals(vehicle, result);
        verify(vehicleMapper).toVehicleJpaEntity(vehicle);
        verify(vehicleJpaRepository).save(vehicleJpaEntity);
        verify(vehicleMapper).toVehicle(vehicleJpaEntity);
    }

}
