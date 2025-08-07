package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import com.fiap.soat12.tc_group_7.entity.VehicleService;
import com.fiap.soat12.tc_group_7.mapper.VehicleServiceMapper;
import com.fiap.soat12.tc_group_7.repository.VehicleServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceServiceTest {

    @InjectMocks
    private VehicleServiceService vehicleServiceService;

    @Mock
    private VehicleServiceRepository vehicleServiceRepository;

    @Mock
    private VehicleServiceMapper vehicleServiceMapper;

    @Test
    void getAllActiveVehicleServices_withSuccess() {
        // Arrange
        VehicleService service1 = VehicleService.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(150))
                .active(true)
                .build();
        VehicleService service2 = VehicleService.builder()
                .id(2L)
                .name("Balanceamento")
                .value(BigDecimal.valueOf(80))
                .active(true)
                .build();
        List<VehicleService> serviceList = List.of(service1, service2);

        when(vehicleServiceRepository.findAllByActiveTrue()).thenReturn(serviceList);

        VehicleServiceResponseDTO dto1 = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(150))
                .build();
        VehicleServiceResponseDTO dto2 = VehicleServiceResponseDTO.builder()
                .id(2L)
                .name("Balanceamento")
                .value(BigDecimal.valueOf(80))
                .build();

        when(vehicleServiceMapper.toVehicleServiceResponseDTO(service1)).thenReturn(dto1);
        when(vehicleServiceMapper.toVehicleServiceResponseDTO(service2)).thenReturn(dto2);

        // Act
        List<VehicleServiceResponseDTO> result = vehicleServiceService.getAllActiveVehicleServices();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Troca de óleo", result.get(0).getName());
        assertEquals("Balanceamento", result.get(1).getName());
        verify(vehicleServiceRepository).findAllByActiveTrue();
        verify(vehicleServiceMapper, times(2)).toVehicleServiceResponseDTO(any(VehicleService.class));
    }

    @Test
    void testCreateVehicleService() {
        VehicleServiceRequestDTO request = VehicleServiceRequestDTO.builder()
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(120))
                .build();
        VehicleService entity = VehicleService.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(120))
                .active(true)
                .build();
        VehicleServiceResponseDTO response = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(120))
                .build();

        when(vehicleServiceRepository.save(any(VehicleService.class))).thenReturn(entity);
        when(vehicleServiceMapper.toVehicleServiceResponseDTO(entity)).thenReturn(response);

        VehicleServiceResponseDTO result = vehicleServiceService.create(request);

        assertNotNull(result);
        assertEquals("Troca de óleo", result.getName());
        verify(vehicleServiceRepository).save(any(VehicleService.class));
    }

    @Test
    void testUpdateVehicleService() {
        Long id = 1L;
        VehicleServiceRequestDTO request = VehicleServiceRequestDTO.builder()
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100))
                .build();
        VehicleService existing = VehicleService.builder()
                .id(id)
                .name("Antigo nome")
                .value(BigDecimal.valueOf(80))
                .active(true)
                .build();
        VehicleService updated = VehicleService.builder()
                .id(id)
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100))
                .active(true)
                .build();
        VehicleServiceResponseDTO response = VehicleServiceResponseDTO.builder()
                .id(id)
                .name("Alinhamento")
                .value(BigDecimal.valueOf(100))
                .build();

        when(vehicleServiceRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(existing));
        when(vehicleServiceRepository.save(existing)).thenReturn(updated);
        when(vehicleServiceMapper.toVehicleServiceResponseDTO(updated)).thenReturn(response);

        VehicleServiceResponseDTO result = vehicleServiceService.update(id, request);

        assertEquals("Alinhamento", result.getName());
        assertEquals(BigDecimal.valueOf(100), result.getValue());
        verify(vehicleServiceRepository).save(existing);
    }

    @Test
    void testDeactivateVehicleService() {
        Long id = 1L;

        VehicleService existing = VehicleService.builder()
                .id(id)
                .name("Serviço ativo")
                .value(BigDecimal.valueOf(150))
                .active(true)
                .build();

        when(vehicleServiceRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(existing));

        vehicleServiceService.deactivate(id);

        assertFalse(existing.getActive());
        verify(vehicleServiceRepository).save(existing);
    }

}
