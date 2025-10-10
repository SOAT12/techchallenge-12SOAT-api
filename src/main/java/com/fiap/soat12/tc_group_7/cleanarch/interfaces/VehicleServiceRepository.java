package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;

import java.util.List;
import java.util.Optional;

public interface VehicleServiceRepository {

    List<VehicleService> findAll();

    Optional<VehicleService> findById(Long id);

    Long save(VehicleService vehicleService);

    void update(VehicleService service);

}
