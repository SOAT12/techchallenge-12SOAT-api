package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleServiceGateway;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleServiceRepository;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehicleServicePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleServiceUseCase;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleServiceController {

    private final VehicleServiceRepository dataSource;
    private final VehicleServicePresenter vehicleServicePresenter;

    public VehicleServiceController(VehicleServiceRepository dataSource) {
        this.dataSource = dataSource;
        this.vehicleServicePresenter = new VehicleServicePresenter();
    }

    public List<VehicleServiceResponseDTO> getAllActiveVehicleServices() {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);

        List<VehicleService> vehicleServices = vehicleServiceUserCase.getAllActiveVehicleServices();
        return vehicleServices.stream()
                .map(vehicleServicePresenter::toVehicleServiceResponseDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleServiceResponseDTO> getAllVehicleServices() {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);

        List<VehicleService> vehicleServices = vehicleServiceUserCase.getAllVehicleServices();
        return vehicleServices.stream()
                .map(vehicleServicePresenter::toVehicleServiceResponseDTO)
                .collect(Collectors.toList());
    }

    public VehicleServiceResponseDTO getById(Long id) {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);

        VehicleService vehicleService = vehicleServiceUserCase.getById(id);
        return vehicleServicePresenter.toVehicleServiceResponseDTO(vehicleService);
    }

    public VehicleServiceResponseDTO create(VehicleServiceRequestDTO dto) {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);

        VehicleService vehicleService = vehicleServiceUserCase.create(dto);
        return vehicleServicePresenter.toVehicleServiceResponseDTO(vehicleService);
    }

    public VehicleServiceResponseDTO update(Long id, VehicleServiceRequestDTO dto) {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);

        VehicleService vehicleService = vehicleServiceUserCase.update(id, dto);
        return vehicleServicePresenter.toVehicleServiceResponseDTO(vehicleService);
    }

    public void deactivate(Long id) {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);
        vehicleServiceUserCase.deactivate(id);
    }

    public void activate(Long id) {
        VehicleServiceGateway vehicleServiceGateway = new VehicleServiceGateway(dataSource);
        VehicleServiceUseCase vehicleServiceUserCase = new VehicleServiceUseCase(vehicleServiceGateway);
        vehicleServiceUserCase.activate(id);
    }

}
