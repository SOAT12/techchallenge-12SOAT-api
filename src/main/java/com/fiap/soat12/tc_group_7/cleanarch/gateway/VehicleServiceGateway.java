package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VehicleServiceGateway {

    private final VehicleServiceRepository vehicleServiceRepository;

    public List<VehicleService> findAll() {
        return vehicleServiceRepository.findAll().stream()
                .map(this::toVehicleService)
                .collect(Collectors.toList());
    }

    public Optional<VehicleService> findById(Long id) {
        return vehicleServiceRepository.findById(id)
                .map(this::toVehicleService);
    }

    public Long save(VehicleService vehicleService) {
        return vehicleServiceRepository.save(this.toVehicleServiceJpaEntity(vehicleService));
    }

    public void update(VehicleService vehicleService) {
        vehicleServiceRepository.update(this.toVehicleServiceJpaEntity(vehicleService));
    }

    private VehicleServiceJpaEntity toVehicleServiceJpaEntity(VehicleService service) {
        return VehicleServiceJpaEntity.builder()
                .id(service.getId())
                .name(service.getName())
                .value(service.getValue())
                .active(service.getActive())
                .build();
    }

    private VehicleService toVehicleService(VehicleServiceJpaEntity entity) {
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
