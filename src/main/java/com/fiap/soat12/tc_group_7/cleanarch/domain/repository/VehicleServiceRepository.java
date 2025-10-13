package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleServiceRepository {

    List<VehicleServiceJpaEntity> findAll();

    Optional<VehicleServiceJpaEntity> findById(Long id);

    Optional<VehicleServiceJpaEntity> findByName(String name);

    Long save(VehicleServiceJpaEntity vehicleService);

    void update(VehicleServiceJpaEntity service);

}
