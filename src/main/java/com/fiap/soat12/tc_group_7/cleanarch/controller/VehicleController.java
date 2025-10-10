package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleGateway;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleRepository;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehiclePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleUseCase;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;

import java.util.List;

public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final VehiclePresenter vehiclePresenter;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
        this.vehiclePresenter = new VehiclePresenter();
    }

    public VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO) {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        Vehicle vehicle = vehicleUseCase.create(requestDTO);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public VehicleResponseDTO getVehicleById(Long id) {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        Vehicle vehicle = vehicleUseCase.getVehicleById(id);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public VehicleResponseDTO getVehicleByLicensePlate(String licensePlate) {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        Vehicle vehicle = vehicleUseCase.getVehicleByLicensePlate(licensePlate);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        List<Vehicle> vehicles = vehicleUseCase.getAllVehicles();
        return vehicles.stream()
                .map(vehiclePresenter::toVehicleResponseDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getAllVehiclesActive() {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        List<Vehicle> vehicles = vehicleUseCase.getAllVehiclesActive();
        return vehicles.stream()
                .map(vehiclePresenter::toVehicleResponseDTO)
                .toList();
    }

    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO requestDTO) {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        Vehicle vehicle = vehicleUseCase.updateVehicle(id, requestDTO);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public void deleteVehicle(Long id) {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        vehicleUseCase.deleteVehicle(id);
    }

    public void reactivateVehicle(Long id) {
        VehicleGateway vehicleGateway = new VehicleGateway(vehicleRepository);
        VehicleUseCase vehicleUseCase = new VehicleUseCase(vehicleGateway);

        vehicleUseCase.reactivateVehicle(id);
    }

}
