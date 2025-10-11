package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleJpaEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VehicleGateway {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll().stream()
                .map(this::toVehicle)
                .collect(Collectors.toList());
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id)
                .map(this::toVehicle);
    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate)
                .map(this::toVehicle);
    }

    public Vehicle save(Vehicle vehicle) {
        VehicleJpaEntity vehicleJpaEntity = vehicleRepository.save(this.toVehicleJpaEntity(vehicle));
        return this.toVehicle(vehicleJpaEntity);
    }

    private VehicleJpaEntity toVehicleJpaEntity(Vehicle vehicle) {
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

    private Vehicle toVehicle(VehicleJpaEntity vehicleJpaEntity) {
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

}
