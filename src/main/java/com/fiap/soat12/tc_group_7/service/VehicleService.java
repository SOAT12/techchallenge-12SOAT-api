package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Vehicle;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.VehicleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService (VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public VehicleResponseDTO createVehicle(VehicleRequestDTO vehicleRequestDTO) {

        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleRequestDTO, vehicle);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToResponseDTO(savedVehicle);
    }

    @Transactional(readOnly = true)
    public Optional<VehicleResponseDTO> findById(Long id) {
        return vehicleRepository.findById(id).filter(Vehicle::getActive)
                .map(this::convertToResponseDTO);
    }

    public Optional<VehicleResponseDTO> findByLicensePlate(String licensePlate) {
        return Optional.ofNullable(vehicleRepository.findByLicensePlate(licensePlate).filter(Vehicle::getActive)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado")));
    }

    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> findAll() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehicleResponseDTO> findAllVehiclesActive() {
        return vehicleRepository.findAllByActiveTrue().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<VehicleResponseDTO> updateVehicle(Long id, VehicleRequestDTO vehicleRequestDTO) {
        return Optional.ofNullable(vehicleRepository.findById(id)
                .map(existingVehicle -> {

                    BeanUtils.copyProperties(vehicleRequestDTO, existingVehicle);
                    Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);

                    return convertToResponseDTO(updatedVehicle);
                }).orElseThrow(() -> new NotFoundException("Veículo não encontrado")));
    }

    @Transactional
    public boolean logicallyDeleteVehicle(Long id) {
        vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicle.setActive(false);
                    vehicleRepository.save(vehicle);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        return false;
    }

    @Transactional
    public Optional<VehicleResponseDTO> reactivateVehicle(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                   vehicle.setActive(true);
                    Vehicle reactivatedVehicle = vehicleRepository.save(vehicle);
                    return convertToResponseDTO(reactivatedVehicle);
                });
    }

    private VehicleResponseDTO convertToResponseDTO(Vehicle savedVehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        BeanUtils.copyProperties(savedVehicle, dto);
        return dto;
    }
}