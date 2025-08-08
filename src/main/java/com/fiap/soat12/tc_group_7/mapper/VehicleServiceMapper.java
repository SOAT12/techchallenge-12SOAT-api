package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import com.fiap.soat12.tc_group_7.entity.VehicleService;
import org.springframework.stereotype.Component;

@Component
public class VehicleServiceMapper {

    public VehicleServiceResponseDTO toVehicleServiceResponseDTO(VehicleService vehicleService) {
        return VehicleServiceResponseDTO.builder()
                .id(vehicleService.getId())
                .name(vehicleService.getName())
                .value(vehicleService.getValue())
                .build();
    }

    public VehicleService toVehicleService(VehicleServiceRequestDTO dto) {
        return VehicleService.builder()
                .name(dto.getName())
                .value(dto.getValue())
                .active(true)
                .build();
    }

}
