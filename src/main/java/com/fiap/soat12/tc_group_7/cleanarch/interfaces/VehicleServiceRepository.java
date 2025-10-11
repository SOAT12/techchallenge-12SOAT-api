package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceJpaEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleServiceRepository {

    List<VehicleServiceJpaEntity> findAll();

    Optional<VehicleServiceJpaEntity> findById(Long id);

    Long save(VehicleServiceJpaEntity vehicleService);

    void update(VehicleServiceJpaEntity service);

}
