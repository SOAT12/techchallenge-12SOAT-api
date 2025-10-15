package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.VehicleServiceUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.VehicleServicePresenter;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VehicleServiceControllerTest {

    @Mock
    private VehicleServiceUseCase vehicleServiceUseCase;

    @Mock
    private VehicleServicePresenter vehicleServicePresenter;

    @InjectMocks
    private VehicleServiceController vehicleServiceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class GetAllActiveVehicleServices {
        @Test
        void shouldGetAllActiveVehicleServices() {
            // Arrange
            when(vehicleServiceUseCase.getAllActiveVehicleServices()).thenReturn(List.of(VehicleService.builder().build()));
            when(vehicleServicePresenter.toVehicleServiceResponseDTO(any())).thenReturn(new VehicleServiceResponseDTO());

            // Act
            List<VehicleServiceResponseDTO> result = vehicleServiceController.getAllActiveVehicleServices();

            // Assert
            assertFalse(result.isEmpty());
            verify(vehicleServiceUseCase).getAllActiveVehicleServices();
        }
    }

    @Nested
    class GetAllVehicleServices {
        @Test
        void shouldGetAllVehicleServices() {
            // Arrange
            when(vehicleServiceUseCase.getAllVehicleServices()).thenReturn(List.of(VehicleService.builder().build()));
            when(vehicleServicePresenter.toVehicleServiceResponseDTO(any())).thenReturn(new VehicleServiceResponseDTO());

            // Act
            List<VehicleServiceResponseDTO> result = vehicleServiceController.getAllVehicleServices();

            // Assert
            assertFalse(result.isEmpty());
            verify(vehicleServiceUseCase).getAllVehicleServices();
        }
    }

    @Nested
    class GetById {
        @Test
        void shouldGetById() {
            // Arrange
            Long id = 1L;
            when(vehicleServiceUseCase.getById(id)).thenReturn(VehicleService.builder().build());
            when(vehicleServicePresenter.toVehicleServiceResponseDTO(any())).thenReturn(new VehicleServiceResponseDTO());

            // Act
            VehicleServiceResponseDTO result = vehicleServiceController.getById(id);

            // Assert
            assertNotNull(result);
            verify(vehicleServiceUseCase).getById(id);
        }
    }

    @Nested
    class Create {
        @Test
        void shouldCreate() {
            // Arrange
            VehicleServiceRequestDTO dto = new VehicleServiceRequestDTO();
            when(vehicleServiceUseCase.create(dto)).thenReturn(VehicleService.builder().build());
            when(vehicleServicePresenter.toVehicleServiceResponseDTO(any())).thenReturn(new VehicleServiceResponseDTO());

            // Act
            VehicleServiceResponseDTO result = vehicleServiceController.create(dto);

            // Assert
            assertNotNull(result);
            verify(vehicleServiceUseCase).create(dto);
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdate() {
            // Arrange
            Long id = 1L;
            VehicleServiceRequestDTO dto = new VehicleServiceRequestDTO();
            when(vehicleServiceUseCase.update(id, dto)).thenReturn(VehicleService.builder().build());
            when(vehicleServicePresenter.toVehicleServiceResponseDTO(any())).thenReturn(new VehicleServiceResponseDTO());

            // Act
            VehicleServiceResponseDTO result = vehicleServiceController.update(id, dto);

            // Assert
            assertNotNull(result);
            verify(vehicleServiceUseCase).update(id, dto);
        }
    }

    @Nested
    class Deactivate {
        @Test
        void shouldDeactivate() {
            // Arrange
            Long id = 1L;

            // Act
            vehicleServiceController.deactivate(id);

            // Assert
            verify(vehicleServiceUseCase).deactivate(id);
        }
    }

    @Nested
    class Activate {
        @Test
        void shouldActivate() {
            // Arrange
            Long id = 1L;

            // Act
            vehicleServiceController.activate(id);

            // Assert
            verify(vehicleServiceUseCase).activate(id);
        }
    }
}