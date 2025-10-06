package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public List<Vehicle> findAll() {
        return vehicleJpaRepository.findAll().stream()
                .map(this::toVehicle)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehicleJpaRepository.findById(id)
                .map(this::toVehicle);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return vehicleJpaRepository.findByLicensePlate(licensePlate)
                .map(this::toVehicle);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        var vehicleJpaEntity = vehicleJpaRepository.save(this.toVehicleJpaEntity(vehicle));
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
