package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    List<Vehicle> findAll();

    Optional<Vehicle> findById(Long id);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Vehicle save(Vehicle vehicle);

}
