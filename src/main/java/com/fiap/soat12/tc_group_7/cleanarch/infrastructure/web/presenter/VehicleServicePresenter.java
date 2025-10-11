package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;

public class VehicleServicePresenter {

    public VehicleServiceResponseDTO toVehicleServiceResponseDTO(VehicleService vehicleService) {
        return VehicleServiceResponseDTO.builder()
                .id(vehicleService.getId())
                .name(vehicleService.getName())
                .value(vehicleService.getValue())
                .build();
    }

}
