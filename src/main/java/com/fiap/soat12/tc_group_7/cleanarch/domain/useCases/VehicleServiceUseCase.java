package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleServiceGateway;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VehicleServiceUseCase {

    public static final String SERVICE_NOT_FOUND_MESSAGE = "Serviço não encontrado.";

    private final VehicleServiceGateway vehicleServiceGateway;

    public List<VehicleService> getAllActiveVehicleServices() {
        return vehicleServiceGateway.findAll().stream()
                .filter(VehicleService::getActive)
                .collect(Collectors.toList());
    }

    public List<VehicleService> getAllVehicleServices() {
        return vehicleServiceGateway.findAll();
    }

    public VehicleService getById(Long id) {
        return vehicleServiceGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND_MESSAGE));
    }

    public VehicleService create(VehicleServiceRequestDTO dto) {
        VehicleService vehicleService = VehicleService.builder()
                .name(dto.getName())
                .value(dto.getValue())
                .active(Boolean.TRUE)
                .build();
        Long id = vehicleServiceGateway.save(vehicleService);
        vehicleService.setId(id);
        return vehicleService;
    }

    public VehicleService update(Long id, VehicleServiceRequestDTO dto) {
        VehicleService vehicleService = vehicleServiceGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND_MESSAGE));

        vehicleService.setName(dto.getName());
        vehicleService.setValue(dto.getValue());

        vehicleServiceGateway.update(vehicleService);
        return vehicleService;
    }

    public void deactivate(Long id) {
        VehicleService vehicleService = vehicleServiceGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND_MESSAGE));
        vehicleService.setActive(Boolean.FALSE);
        vehicleServiceGateway.update(vehicleService);
    }

    public void activate(Long id) {
        VehicleService vehicleService = vehicleServiceGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND_MESSAGE));
        vehicleService.setActive(Boolean.TRUE);
        vehicleServiceGateway.update(vehicleService);
    }

}
