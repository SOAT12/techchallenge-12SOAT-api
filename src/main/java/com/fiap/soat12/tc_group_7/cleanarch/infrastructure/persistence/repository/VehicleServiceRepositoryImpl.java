package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleServiceRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleServiceMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.VehicleServiceJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehicleServiceRepositoryImpl implements VehicleServiceRepository {

    private final VehicleServiceMapper vehicleServiceMapper;
    private final VehicleServiceJpaRepository vehicleServiceJpaRepository;

    @Override
    public List<VehicleService> findAll() {
        return vehicleServiceJpaRepository.findAll().stream()
                .map(vehicleServiceMapper::toVehicleService)
                .toList();
    }

    @Override
    public Optional<VehicleService> findById(Long id) {
        return vehicleServiceJpaRepository.findById(id)
                .map(vehicleServiceMapper::toVehicleService);
    }

    @Override
    public Optional<VehicleService> findByName(String name) {
        return vehicleServiceJpaRepository.findByName(name)
                .map(vehicleServiceMapper::toVehicleService);
    }

    @Override
    public Long save(VehicleService vehicleService) {
        var vehicleServiceJpaEntity = vehicleServiceJpaRepository.save(vehicleServiceMapper.toVehicleServiceJpaEntity(vehicleService));
        return vehicleServiceJpaEntity.getId();
    }

    @Override
    public void update(VehicleService vehicleService) {
        vehicleServiceJpaRepository.save(vehicleServiceMapper.toVehicleServiceJpaEntity(vehicleService));
    }

}
