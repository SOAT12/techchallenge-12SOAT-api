package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.VehicleJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehicleJpaRepository vehicleJpaRepository;

    @Override
    public List<VehicleJpaEntity> findAll() {
        return vehicleJpaRepository.findAll();
    }

    @Override
    public Optional<VehicleJpaEntity> findById(Long id) {
        return vehicleJpaRepository.findById(id);
    }

    @Override
    public Optional<VehicleJpaEntity> findByLicensePlate(String licensePlate) {
        return vehicleJpaRepository.findByLicensePlate(licensePlate);
    }

    @Override
    public VehicleJpaEntity save(VehicleJpaEntity vehicle) {
        return vehicleJpaRepository.save(vehicle);
    }

}
