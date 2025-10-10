package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.entity.VehicleService;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class VehicleServiceMapperTest {

    private final VehicleServiceMapper mapper = new VehicleServiceMapper();

    @Test
    void toVehicleServiceResponseDTO_withSuccess() {
        // Arrange
        VehicleService vehicleService = VehicleService.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(BigDecimal.valueOf(150))
                .active(true)
                .build();

        // Act
        VehicleServiceResponseDTO dto = mapper.toVehicleServiceResponseDTO(vehicleService);

        // Assert
        assertEquals(vehicleService.getId(), dto.getId());
        assertEquals(vehicleService.getName(), dto.getName());
        assertEquals(vehicleService.getValue(), dto.getValue());
    }

}
