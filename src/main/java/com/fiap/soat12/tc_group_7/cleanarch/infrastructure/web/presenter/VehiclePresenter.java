package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;

public class VehiclePresenter {

    public VehicleResponseDTO toVehicleResponseDTO(Vehicle vehicle) {
        return VehicleResponseDTO.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .active(vehicle.getActive())
                .build();
    }
}
