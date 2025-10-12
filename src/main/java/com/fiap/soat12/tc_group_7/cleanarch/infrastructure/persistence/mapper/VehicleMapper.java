package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;

public class VehicleMapper {

    public Vehicle toVehicle(VehicleJpaEntity vehicleJpaEntity) {
        return Vehicle.builder()
                .id(vehicleJpaEntity.getId())
                .licensePlate(vehicleJpaEntity.getLicensePlate())
                .brand(vehicleJpaEntity.getBrand())
                .model(vehicleJpaEntity.getModel())
                .year(vehicleJpaEntity.getYear())
                .color(vehicleJpaEntity.getColor())
                .active(vehicleJpaEntity.getActive())
                .createdAt(vehicleJpaEntity.getCreatedAt())
                .updatedAt(vehicleJpaEntity.getUpdatedAt())
                .build();
    }

    public VehicleJpaEntity toVehicleJpaEntity(Vehicle vehicle) {
        return VehicleJpaEntity.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .active(vehicle.getActive())
                .build();
    }

}
