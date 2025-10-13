package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;

public class VehicleServiceMapper {

    public VehicleServiceJpaEntity toVehicleServiceJpaEntity(VehicleService service) {
        return VehicleServiceJpaEntity.builder()
                .id(service.getId())
                .name(service.getName())
                .value(service.getValue())
                .active(service.getActive())
                .build();
    }

    public VehicleService toVehicleService(VehicleServiceJpaEntity entity) {
        return VehicleService.builder()
                .id(entity.getId())
                .name(entity.getName())
                .value(entity.getValue())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
