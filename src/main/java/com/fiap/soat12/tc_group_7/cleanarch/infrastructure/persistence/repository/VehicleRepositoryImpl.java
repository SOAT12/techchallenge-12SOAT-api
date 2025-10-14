package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.VehicleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehicleMapper vehicleMapper;
    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public List<Vehicle> findAll() {
        return vehicleJpaRepository.findAll().stream()
                .map(vehicleMapper::toVehicle)
                .toList();
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehicleJpaRepository.findById(id)
                .map(vehicleMapper::toVehicle);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return vehicleJpaRepository.findByLicensePlate(licensePlate)
                .map(vehicleMapper::toVehicle);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        var savedVehicle = vehicleJpaRepository.save(vehicleMapper.toVehicleJpaEntity(vehicle));
        return vehicleMapper.toVehicle(savedVehicle);
    }

}
