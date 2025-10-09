package com.fiap.soat12.tc_group_7.cleanarch.usercase;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleServiceGateway;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleServiceUseCase;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleServiceUseCaseTest {

    @Mock
    private VehicleServiceGateway vehicleServiceGateway;

    private VehicleServiceUseCase vehicleServiceUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        vehicleServiceUseCase = new VehicleServiceUseCase(vehicleServiceGateway);
    }

    @Test
    void getAllActiveVehicleServices_returnsOnlyActive() {
        VehicleService activeService = VehicleService.builder().id(1L).active(true).build();
        VehicleService inactiveService = VehicleService.builder().id(2L).active(false).build();

        when(vehicleServiceGateway.findAll()).thenReturn(List.of(activeService, inactiveService));

        List<VehicleService> result = vehicleServiceUseCase.getAllActiveVehicleServices();

        assertEquals(1, result.size());
        assertTrue(result.getFirst().getActive());
        verify(vehicleServiceGateway).findAll();
    }

    @Test
    void getAllVehicleServices_returnsAll() {
        List<VehicleService> services = List.of(
                VehicleService.builder().id(1L).build(),
                VehicleService.builder().id(2L).build()
        );

        when(vehicleServiceGateway.findAll()).thenReturn(services);

        List<VehicleService> result = vehicleServiceUseCase.getAllVehicleServices();

        assertEquals(2, result.size());
        verify(vehicleServiceGateway).findAll();
    }

    @Test
    void getById_found() {
        Long id = 1L;
        VehicleService service = VehicleService.builder().id(id).build();

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.of(service));

        VehicleService result = vehicleServiceUseCase.getById(id);

        assertEquals(id, result.getId());
        verify(vehicleServiceGateway).findById(id);
    }

    @Test
    void getById_notFound_throwsException() {
        Long id = 1L;

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            vehicleServiceUseCase.getById(id);
        });

        assertEquals(VehicleServiceUseCase.SERVICE_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(vehicleServiceGateway).findById(id);
    }

    @Test
    void create_savesAndReturnsVehicleService() {
        VehicleServiceRequestDTO dto = VehicleServiceRequestDTO.builder()
                .name("Oil Change")
                .value(BigDecimal.valueOf(100.0))
                .build();

        when(vehicleServiceGateway.save(any(VehicleService.class))).thenReturn(1L);

        VehicleService result = vehicleServiceUseCase.create(dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getValue(), result.getValue());
        assertEquals(1L, result.getId());
        assertTrue(result.getActive());
        verify(vehicleServiceGateway).save(any(VehicleService.class));
    }

    @Test
    void update_found_updatesAndReturns() {
        Long id = 1L;
        VehicleService existingService = VehicleService.builder()
                .id(id)
                .name("Old Name")
                .value(BigDecimal.valueOf(50.0))
                .build();

        VehicleServiceRequestDTO dto = VehicleServiceRequestDTO.builder()
                .name("New Name")
                .value(BigDecimal.valueOf(75.0))
                .build();

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.of(existingService));

        VehicleService result = vehicleServiceUseCase.update(id, dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getValue(), result.getValue());
        verify(vehicleServiceGateway).findById(id);
        verify(vehicleServiceGateway).update(existingService);
    }

    @Test
    void update_notFound_throwsException() {
        Long id = 1L;
        VehicleServiceRequestDTO dto = VehicleServiceRequestDTO.builder().build();

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            vehicleServiceUseCase.update(id, dto);
        });

        assertEquals(VehicleServiceUseCase.SERVICE_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(vehicleServiceGateway).findById(id);
        verify(vehicleServiceGateway, never()).update(any());
    }

    @Test
    void deactivate_found_setsActiveFalse() {
        Long id = 1L;
        VehicleService service = VehicleService.builder()
                .id(id)
                .active(true)
                .build();

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.of(service));

        vehicleServiceUseCase.deactivate(id);

        assertFalse(service.getActive());
        verify(vehicleServiceGateway).update(service);
    }

    @Test
    void deactivate_notFound_throwsException() {
        Long id = 1L;

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            vehicleServiceUseCase.deactivate(id);
        });

        assertEquals(VehicleServiceUseCase.SERVICE_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(vehicleServiceGateway).findById(id);
        verify(vehicleServiceGateway, never()).update(any());
    }

    @Test
    void activate_found_setsActiveTrue() {
        Long id = 1L;
        VehicleService service = VehicleService.builder()
                .id(id)
                .active(false)
                .build();

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.of(service));

        vehicleServiceUseCase.activate(id);

        assertTrue(service.getActive());
        verify(vehicleServiceGateway).update(service);
    }

    @Test
    void activate_notFound_throwsException() {
        Long id = 1L;

        when(vehicleServiceGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            vehicleServiceUseCase.activate(id);
        });

        assertEquals(VehicleServiceUseCase.SERVICE_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(vehicleServiceGateway).findById(id);
        verify(vehicleServiceGateway, never()).update(any());
    }

}
