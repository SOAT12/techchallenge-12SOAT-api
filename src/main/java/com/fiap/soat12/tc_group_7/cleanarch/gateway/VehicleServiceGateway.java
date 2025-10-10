package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehicleServiceGateway {

    private final VehicleServiceRepository vehicleServiceRepository;

    public List<VehicleService> findAll() {
        return vehicleServiceRepository.findAll();
    }

    public Optional<VehicleService> findById(Long id) {
        return vehicleServiceRepository.findById(id);
    }

    public Long save(VehicleService vehicleService) {
        return vehicleServiceRepository.save(vehicleService);
    }

    public void update(VehicleService vehicleService) {
        vehicleServiceRepository.update(vehicleService);
    }

}
