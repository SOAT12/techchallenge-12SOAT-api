package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VehicleMapperTest {

    private VehicleMapper vehicleMapper;

    @BeforeEach
    void setUp() {
        vehicleMapper = new VehicleMapper();
    }

    @Test
    void toVehicle_shouldMapAllFieldsCorrectly() {
        VehicleJpaEntity entity = VehicleJpaEntity.builder()
                .id(1L)
                .licensePlate("XYZ1234")
                .brand("Ford")
                .model("Focus")
                .year(2020)
                .color("Branco")
                .active(true)
                .build();

        Vehicle vehicle = vehicleMapper.toVehicle(entity);

        assertNotNull(vehicle);
        assertEquals(entity.getId(), vehicle.getId());
        assertEquals(entity.getLicensePlate(), vehicle.getLicensePlate());
        assertEquals(entity.getBrand(), vehicle.getBrand());
        assertEquals(entity.getModel(), vehicle.getModel());
        assertEquals(entity.getYear(), vehicle.getYear());
        assertEquals(entity.getColor(), vehicle.getColor());
        assertEquals(entity.getActive(), vehicle.getActive());
        assertEquals(entity.getCreatedAt(), vehicle.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), vehicle.getUpdatedAt());
    }

    @Test
    void toVehicleJpaEntity_shouldMapAllFieldsCorrectly() {
        Vehicle vehicle = Vehicle.builder()
                .id(2L)
                .licensePlate("ABC5678")
                .brand("Chevrolet")
                .model("Onix")
                .year(2022)
                .color("Prata")
                .active(false)
                .build();

        VehicleJpaEntity entity = vehicleMapper.toVehicleJpaEntity(vehicle);

        assertNotNull(entity);
        assertEquals(vehicle.getId(), entity.getId());
        assertEquals(vehicle.getLicensePlate(), entity.getLicensePlate());
        assertEquals(vehicle.getBrand(), entity.getBrand());
        assertEquals(vehicle.getModel(), entity.getModel());
        assertEquals(vehicle.getYear(), entity.getYear());
        assertEquals(vehicle.getColor(), entity.getColor());
        assertEquals(vehicle.getActive(), entity.getActive());
    }

}
