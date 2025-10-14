package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.*;
import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import com.fiap.soat12.tc_group_7.dto.serviceorder.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.serviceorder.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.dto.serviceorder.ServiceOrderStatusResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceOrderPresenterTest {

    private final ServiceOrderPresenter serviceOrderPresenter = new ServiceOrderPresenter();

    @Test
    void toServiceOrderResponseDTO_shouldReturnNull_whenInputIsNull() {
        ServiceOrderResponseDTO result = serviceOrderPresenter.toServiceOrderResponseDTO(null);
        assertNull(result);
    }

    @Test
    void toServiceOrderResponseDTO_shouldMapAllFields_whenInputIsValid() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("João")
                .cpf("123.456.789-00")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .id(10L)
                .licensePlate("ABC1234")
                .model("Civic")
                .build();

        Employee employee = Employee.builder()
                .id(5L)
                .name("Carlos")
                .build();

        VehicleService service1 = VehicleService.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(new BigDecimal("100.00"))
                .build();

        VehicleService service2 = VehicleService.builder()
                .id(2L)
                .name("Alinhamento")
                .value(new BigDecimal("50.00"))
                .build();

        Stock stock1 = Stock.builder()
                .id(UUID.randomUUID())
                .toolName("Filtro de óleo")
                .quantity(1)
                .value(new BigDecimal("30.00"))
                .build();

        Stock stock2 = Stock.builder()
                .id(UUID.randomUUID())
                .toolName("Parafuso")
                .quantity(4)
                .value(new BigDecimal("5.00"))
                .build();

        ServiceOrder order = ServiceOrder.builder()
                .id(100L)
                .status(Status.FINISHED)
                .notes("Tudo certo.")
                .createdAt(new Date())
                .updatedAt(new Date())
                .finishedAt(new Date())
                .totalValue(new BigDecimal("185.00"))
                .customer(customer)
                .vehicle(vehicle)
                .employee(employee)
                .services(Set.of(service1, service2))
                .stockItems(Set.of(stock1, stock2))
                .build();

        // Act
        ServiceOrderResponseDTO dto = serviceOrderPresenter.toServiceOrderResponseDTO(order);

        // Assert
        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(Status.FINISHED, dto.getStatus());
        assertEquals("Tudo certo.", dto.getNotes());
        assertEquals(new BigDecimal("185.00"), dto.getTotalValue());

        assertNotNull(dto.getCustomer());
        assertEquals(1L, dto.getCustomer().getId());
        assertEquals("João", dto.getCustomer().getName());
        assertEquals("123.456.789-00", dto.getCustomer().getDocument());

        assertNotNull(dto.getVehicle());
        assertEquals(10L, dto.getVehicle().getId());
        assertEquals("ABC1234", dto.getVehicle().getLicensePlate());
        assertEquals("Civic", dto.getVehicle().getModel());

        assertNotNull(dto.getEmployee());
        assertEquals(5L, dto.getEmployee().getId());
        assertEquals("Carlos", dto.getEmployee().getName());

        assertEquals(2, dto.getServices().size());
        assertTrue(dto.getServices().stream().anyMatch(s -> s.getName().equals("Troca de óleo")));
        assertTrue(dto.getServices().stream().anyMatch(s -> s.getName().equals("Alinhamento")));

        assertEquals(2, dto.getStockItems().size());
        assertTrue(dto.getStockItems().stream().anyMatch(s -> s.getToolName().equals("Filtro de óleo")));
        assertTrue(dto.getStockItems().stream().anyMatch(s -> s.getToolName().equals("Parafuso")));
    }

    @Test
    void toAverageExecutionTimeResponseDTO_shouldReturnZero_whenDurationIsZero() {
        // Arrange
        Duration zero = Duration.ZERO;

        // Act
        AverageExecutionTimeResponseDTO result = serviceOrderPresenter.toAverageExecutionTimeResponseDTO(zero);

        // Assert
        assertNotNull(result);
        assertEquals(0L, result.getAverageExecutionTimeHours());
        assertEquals("0 horas, 0 minutos", result.getAverageExecutionTimeFormatted());
    }

    @Test
    void toAverageExecutionTimeResponseDTO_shouldFormatCorrectly_whenDurationIsValid() {
        // Arrange
        Duration duration = Duration.ofHours(3).plusMinutes(45);

        // Act
        AverageExecutionTimeResponseDTO result = serviceOrderPresenter.toAverageExecutionTimeResponseDTO(duration);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getAverageExecutionTimeHours());
        assertEquals("3 horas, 45 minutos", result.getAverageExecutionTimeFormatted());
    }

    @Test
    void toServiceOrderStatusResponseDTO_shouldReturnCorrectLabel() {
        // Arrange
        Status status = Status.APPROVED;

        // Act
        ServiceOrderStatusResponseDTO result = serviceOrderPresenter.toServiceOrderStatusResponseDTO(status);

        // Assert
        assertNotNull(result);
        assertEquals(status.getLabel(), result.getStatus());
    }

}
