package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.VehicleServiceUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.VehicleServicePresenter;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleServiceController {

    private final VehicleServiceUseCase vehicleServiceUseCase;
    private final VehicleServicePresenter vehicleServicePresenter;

    public VehicleServiceController(VehicleServiceUseCase vehicleServiceUseCase, VehicleServicePresenter vehicleServicePresenter) {
        this.vehicleServiceUseCase = vehicleServiceUseCase;
        this.vehicleServicePresenter = vehicleServicePresenter;
    }

    public List<VehicleServiceResponseDTO> getAllActiveVehicleServices() {
        List<VehicleService> vehicleServices = vehicleServiceUseCase.getAllActiveVehicleServices();
        return vehicleServices.stream()
                .map(vehicleServicePresenter::toVehicleServiceResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleServiceResponseDTO> getAllVehicleServices() {
        List<VehicleService> vehicleServices = vehicleServiceUseCase.getAllVehicleServices();
        return vehicleServices.stream()
                .map(vehicleServicePresenter::toVehicleServiceResponseDTO)
                .collect(Collectors.toList());
    }

    public VehicleServiceResponseDTO getById(Long id) {
        VehicleService vehicleService = vehicleServiceUseCase.getById(id);
        return vehicleServicePresenter.toVehicleServiceResponseDTO(vehicleService);
    }

    public VehicleServiceResponseDTO create(VehicleServiceRequestDTO dto) {
        VehicleService vehicleService = vehicleServiceUseCase.create(dto);
        return vehicleServicePresenter.toVehicleServiceResponseDTO(vehicleService);
    }

    public VehicleServiceResponseDTO update(Long id, VehicleServiceRequestDTO dto) {
        VehicleService vehicleService = vehicleServiceUseCase.update(id, dto);
        return vehicleServicePresenter.toVehicleServiceResponseDTO(vehicleService);
    }

    public void deactivate(Long id) {
        vehicleServiceUseCase.deactivate(id);
    }

    public void activate(Long id) {
        vehicleServiceUseCase.activate(id);
    }

}
