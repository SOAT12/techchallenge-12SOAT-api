package com.fiap.soat12.tc_group_7.cleanarch.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Vehicle;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehiclePresenterTest {

    private final VehiclePresenter presenter = new VehiclePresenter();

    @Test
    void toVehicleResponseDTO_withSuccess() {
        // Arrange
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Preto")
                .active(true)
                .build();

        VehicleResponseDTO expectedDTO = VehicleResponseDTO.builder()
                .id(1L)
                .licensePlate("ABC-1234")
                .brand("Toyota")
                .model("Corolla")
                .year(2020)
                .color("Preto")
                .active(true)
                .build();

        // Act
        VehicleResponseDTO actualDTO = presenter.toVehicleResponseDTO(vehicle);

        // Assert
        assertEquals(expectedDTO, actualDTO);
    }
}
