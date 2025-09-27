package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleDataSource;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class VehicleServiceGateway {

    private final VehicleDataSource dataSource;

    public List<VehicleService> findAll() {
        return dataSource.findAll();
    }

    public Optional<VehicleService> findById(Long id) {
        return dataSource.findById(id);
    }

    public Long save(VehicleService vehicleService) {
        return dataSource.save(vehicleService);
    }

    public void update(VehicleService vehicleService) {
        dataSource.update(vehicleService);
    }

}
