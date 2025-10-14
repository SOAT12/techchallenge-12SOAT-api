package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.VehicleServiceController;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class VehicleServiceApiTest {

    @Mock
    private VehicleServiceController vehicleServiceController;

    @InjectMocks
    private VehicleServiceApi vehicleServiceApi;

    private VehicleServiceRequestDTO sampleRequest;
    private VehicleServiceResponseDTO sampleResponse;

    @BeforeEach
    void setup() {
        sampleRequest = new VehicleServiceRequestDTO();
        sampleRequest.setName("Troca de óleo");
        sampleRequest.setValue(new BigDecimal("120.00"));

        sampleResponse = VehicleServiceResponseDTO.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(new BigDecimal("120.00"))
                .build();
    }

    @Test
    void getAllActiveVehicleServices_shouldReturnList() {
        when(vehicleServiceController.getAllActiveVehicleServices()).thenReturn(List.of(sampleResponse));

        List<VehicleServiceResponseDTO> result = vehicleServiceApi.getAllActiveVehicleServices();

        assertEquals(1, result.size());
        assertEquals("Troca de óleo", result.get(0).getName());
        verify(vehicleServiceController).getAllActiveVehicleServices();
    }

    @Test
    void getAllVehicleServices_shouldReturnList() {
        when(vehicleServiceController.getAllVehicleServices()).thenReturn(List.of(sampleResponse));

        List<VehicleServiceResponseDTO> result = vehicleServiceApi.getAllVehicleServices();

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("120.00"), result.get(0).getValue());
        verify(vehicleServiceController).getAllVehicleServices();
    }

    @Test
    void getById_shouldReturnService() {
        Long id = 1L;
        when(vehicleServiceController.getById(id)).thenReturn(sampleResponse);

        VehicleServiceResponseDTO result = vehicleServiceApi.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Troca de óleo", result.getName());
        verify(vehicleServiceController).getById(id);
    }

    @Test
    void create_shouldReturnCreatedService() {
        when(vehicleServiceController.create(sampleRequest)).thenReturn(sampleResponse);

        VehicleServiceResponseDTO result = vehicleServiceApi.create(sampleRequest);

        assertEquals("Troca de óleo", result.getName());
        verify(vehicleServiceController).create(sampleRequest);
    }

    @Test
    void update_shouldReturnUpdatedService() {
        Long id = 1L;
        VehicleServiceRequestDTO updateRequest = new VehicleServiceRequestDTO();
        updateRequest.setName("Alinhamento");
        updateRequest.setValue(new BigDecimal("80.00"));

        VehicleServiceResponseDTO updatedResponse = VehicleServiceResponseDTO.builder()
                .id(id)
                .name("Alinhamento")
                .value(new BigDecimal("80.00"))
                .build();

        when(vehicleServiceController.update(id, updateRequest)).thenReturn(updatedResponse);

        VehicleServiceResponseDTO result = vehicleServiceApi.update(id, updateRequest);

        assertEquals("Alinhamento", result.getName());
        assertEquals(new BigDecimal("80.00"), result.getValue());
        verify(vehicleServiceController).update(id, updateRequest);
    }

    @Test
    void deactivate_shouldCallController() {
        Long id = 1L;

        vehicleServiceApi.deactivate(id);

        verify(vehicleServiceController).deactivate(id);
    }

    @Test
    void activate_shouldCallController() {
        Long id = 1L;

        vehicleServiceApi.activate(id);

        verify(vehicleServiceController).activate(id);
    }
}
