package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VehicleServiceRepositoryImpl implements VehicleServiceRepository {

    private final VehicleServiceJpaRepository vehicleServiceJpaRepository;

    @Override
    public List<VehicleService> findAll() {
        return vehicleServiceJpaRepository.findAll().stream()
                .map(this::toVehicleService)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VehicleService> findById(Long id) {
        return vehicleServiceJpaRepository.findById(id)
                .map(this::toVehicleService);
    }

    @Override
    public Long save(VehicleService vehicleService) {
        var vehicleServiceJpaEntity = vehicleServiceJpaRepository.save(this.toVehicleServiceJpaEntity(vehicleService));
        return vehicleServiceJpaEntity.getId();
    }

    @Override
    public void update(VehicleService vehicleService) {
        vehicleServiceJpaRepository.save(this.toVehicleServiceJpaEntity(vehicleService));
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
