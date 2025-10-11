package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice;

import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehicleServiceRepositoryImpl implements VehicleServiceRepository {

    private final VehicleServiceJpaRepository vehicleServiceJpaRepository;

    @Override
    public List<VehicleServiceJpaEntity> findAll() {
        return vehicleServiceJpaRepository.findAll();
    }

    @Override
    public Optional<VehicleServiceJpaEntity> findById(Long id) {
        return vehicleServiceJpaRepository.findById(id);
    }

    @Override
    public Long save(VehicleServiceJpaEntity vehicleService) {
        var vehicleServiceJpaEntity = vehicleServiceJpaRepository.save(vehicleService);
        return vehicleServiceJpaEntity.getId();
    }

    @Override
    public void update(VehicleServiceJpaEntity vehicleService) {
        vehicleServiceJpaRepository.save(vehicleService);
    }

}
