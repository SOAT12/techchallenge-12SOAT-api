package com.fiap.soat12.tc_group_7.cleanarch.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.VehicleServicePresenter;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleServicePresenterTest {

    private final VehicleServicePresenter presenter = new VehicleServicePresenter();

    @Test
    void toVehicleServiceResponseDTO_withSuccess() {
        // Arrange
        VehicleService vehicleService = VehicleService.builder()
                .id(10L)
                .name("Troca de óleo")
                .value(new BigDecimal("120.50"))
                .build();

        VehicleServiceResponseDTO expectedDTO = VehicleServiceResponseDTO.builder()
                .id(10L)
                .name("Troca de óleo")
                .value(new BigDecimal("120.50"))
                .build();

        // Act
        VehicleServiceResponseDTO actualDTO = presenter.toVehicleServiceResponseDTO(vehicleService);

        // Assert
        assertEquals(expectedDTO, actualDTO);
    }

}
