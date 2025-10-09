package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleJpaEntity;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    List<VehicleJpaEntity> findAll();

    Optional<VehicleJpaEntity> findById(Long id);

    Optional<VehicleJpaEntity> findByLicensePlate(String licensePlate);

    VehicleJpaEntity save(VehicleJpaEntity vehicle);

}
