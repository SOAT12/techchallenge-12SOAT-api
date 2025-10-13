package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleGateway;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VehicleUseCase {

    protected static final String VEHICLE_NOT_FOUND_MESSAGE = "Veículo não encontrado.";
    protected static final String VEHICLE_ALREADY_EXISTS_MESSAGE = "Já existe um veículo cadastrado com a placa informada.";

    private final VehicleGateway vehicleGateway;

    public Vehicle create(VehicleRequestDTO requestDTO) {
        if (vehicleGateway.findByLicensePlate(requestDTO.getLicensePlate()).isPresent()) {
            throw new IllegalArgumentException(VEHICLE_ALREADY_EXISTS_MESSAGE);
        }

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(requestDTO.getLicensePlate())
                .brand(requestDTO.getBrand())
                .model(requestDTO.getModel())
                .year(requestDTO.getYear())
                .color(requestDTO.getColor())
                .build();
        return vehicleGateway.save(vehicle);
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND_MESSAGE));
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return vehicleGateway.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND_MESSAGE));
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleGateway.findAll();
    }

    public List<Vehicle> getAllVehiclesActive() {
        return vehicleGateway.findAll().stream()
                .filter(Vehicle::getActive)
                .toList();
    }

    public Vehicle updateVehicle(Long id, VehicleRequestDTO requestDTO) {
        var vehicle = vehicleGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND_MESSAGE));

        vehicle.setLicensePlate(requestDTO.getLicensePlate());
        vehicle.setBrand(requestDTO.getBrand());
        vehicle.setModel(requestDTO.getModel());
        vehicle.setYear(requestDTO.getYear());
        vehicle.setColor(requestDTO.getColor());

        return vehicleGateway.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        var vehicle = vehicleGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND_MESSAGE));
        vehicle.setActive(Boolean.FALSE);
        vehicleGateway.save(vehicle);
    }

    public void reactivateVehicle(Long id) {
        var vehicle = vehicleGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND_MESSAGE));
        vehicle.setActive(Boolean.TRUE);
        vehicleGateway.save(vehicle);
    }

}
