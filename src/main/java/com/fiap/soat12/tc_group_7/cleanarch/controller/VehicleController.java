package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehiclePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleUseCase;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;

import java.util.List;

public class VehicleController {

    private final VehicleUseCase vehicleUseCase;
    private final VehiclePresenter vehiclePresenter;

    public VehicleController(VehicleUseCase vehicleUseCase, VehiclePresenter vehiclePresenter) {
        this.vehicleUseCase = vehicleUseCase;
        this.vehiclePresenter = vehiclePresenter;
    }

    public VehicleResponseDTO createVehicle(VehicleRequestDTO requestDTO) {
        Vehicle vehicle = vehicleUseCase.create(requestDTO);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public VehicleResponseDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleUseCase.getVehicleById(id);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public VehicleResponseDTO getVehicleByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleUseCase.getVehicleByLicensePlate(licensePlate);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public List<VehicleResponseDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleUseCase.getAllVehicles();
        return vehicles.stream()
                .map(vehiclePresenter::toVehicleResponseDTO)
                .toList();
    }

    public List<VehicleResponseDTO> getAllVehiclesActive() {
        List<Vehicle> vehicles = vehicleUseCase.getAllVehiclesActive();
        return vehicles.stream()
                .map(vehiclePresenter::toVehicleResponseDTO)
                .toList();
    }

    public VehicleResponseDTO updateVehicle(Long id, VehicleRequestDTO requestDTO) {
        Vehicle vehicle = vehicleUseCase.updateVehicle(id, requestDTO);
        return vehiclePresenter.toVehicleResponseDTO(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleUseCase.deleteVehicle(id);
    }

    public void reactivateVehicle(Long id) {
        vehicleUseCase.reactivateVehicle(id);
    }

}
