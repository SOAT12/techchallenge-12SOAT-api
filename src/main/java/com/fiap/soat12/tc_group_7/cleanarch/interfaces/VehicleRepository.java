package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    List<Vehicle> findAll();

    Optional<Vehicle> findById(Long id);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    Vehicle save(Vehicle vehicle);

}
