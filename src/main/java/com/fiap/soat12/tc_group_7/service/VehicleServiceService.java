package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import com.fiap.soat12.tc_group_7.entity.VehicleService;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.mapper.VehicleServiceMapper;
import com.fiap.soat12.tc_group_7.repository.VehicleServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceService {

    private final VehicleServiceRepository vehicleServiceRepository;
    private final VehicleServiceMapper vehicleServiceMapper;

    public List<VehicleServiceResponseDTO> getAllActiveVehicleServices() {
        List<VehicleService> activeServices = vehicleServiceRepository.findAllByActiveTrue();
        return activeServices.stream()
                .map(vehicleServiceMapper::toVehicleServiceResponseDTO)
                .toList();
    }

    public List<VehicleServiceResponseDTO> getAllVehicleServices() {
        List<VehicleService> activeServices = vehicleServiceRepository.findAll();
        return activeServices.stream()
                .map(vehicleServiceMapper::toVehicleServiceResponseDTO)
                .toList();
    }

    public VehicleServiceResponseDTO getById(Long id) {
        VehicleService service = vehicleServiceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado."));
        return vehicleServiceMapper.toVehicleServiceResponseDTO(service);
    }

    public VehicleServiceResponseDTO create(VehicleServiceRequestDTO dto) {
        VehicleService vehicleService = vehicleServiceMapper.toVehicleService(dto);
        vehicleService = vehicleServiceRepository.save(vehicleService);
        return vehicleServiceMapper.toVehicleServiceResponseDTO(vehicleService);
    }

    public VehicleServiceResponseDTO update(Long id, VehicleServiceRequestDTO dto) {
        VehicleService vehicleService = vehicleServiceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado."));

        vehicleService.setName(dto.getName());
        vehicleService.setValue(dto.getValue());

        vehicleService = vehicleServiceRepository.save(vehicleService);
        return vehicleServiceMapper.toVehicleServiceResponseDTO(vehicleService);
    }

    public void deactivate(Long id) {
        VehicleService entity = vehicleServiceRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado."));
        entity.setActive(false);
        vehicleServiceRepository.save(entity);
    }

    public void activate(Long id) {
        VehicleService entity = vehicleServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado."));
        entity.setActive(true);
        vehicleServiceRepository.save(entity);
    }

}
