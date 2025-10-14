package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;

import java.util.List;
import java.util.Optional;

public interface VehicleServiceRepository {

    List<VehicleService> findAll();

    Optional<VehicleService> findById(Long id);

    Optional<VehicleService> findByName(String name);

    Long save(VehicleService vehicleService);

    void update(VehicleService vehicleService);

}
