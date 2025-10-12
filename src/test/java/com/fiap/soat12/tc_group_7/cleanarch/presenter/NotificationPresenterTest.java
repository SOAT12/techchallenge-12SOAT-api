package com.fiap.soat12.tc_group_7.cleanarch.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Notification;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.util.Status;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationPresenterTest {

    private final NotificationPresenter presenter = new NotificationPresenter();

//    @Test
//    void toNotificationResponseDTO_withSuccess() {
//        // Arrange
//        ServiceOrder serviceOrder = ServiceOrder.builder()
//                .id(100L)
//                .status(Status.FINISHED)
//                .totalValue(new BigDecimal("250.00"))
//                .build();
//
//        Employee employee1 = Employee.builder()
//                .id(1L)
//                .name("Carlos")
//                .build();
//
//        Employee employee2 = Employee.builder()
//                .id(2L)
//                .name("Ana")
//                .build();
//
//        Notification notification = Notification.builder()
//                .id(500L)
//                .message("Serviço concluído com sucesso.")
//                .isRead(false)
//                .serviceOrder(serviceOrder)
//                .employees(Set.of(employee1, employee2))
//                .build();
//
//        NotificationResponseDTO.ServiceOrderDTO expectedServiceOrderDTO =
//                NotificationResponseDTO.ServiceOrderDTO.builder()
//                        .id(100L)
//                        .status(Status.FINISHED)
//                        .totalValue(new BigDecimal("250.00"))
//                        .build();
//
//        List<NotificationResponseDTO.EmployeeDTO> expectedEmployees = List.of(
//                NotificationResponseDTO.EmployeeDTO.builder()
//                        .id(1L)
//                        .name("Carlos")
//                        .build(),
//                NotificationResponseDTO.EmployeeDTO.builder()
//                        .id(2L)
//                        .name("Ana")
//                        .build()
//        );
//
//        NotificationResponseDTO expectedDTO = NotificationResponseDTO.builder()
//                .id(500L)
//                .message("Serviço concluído com sucesso.")
//                .isRead(false)
//                .serviceOrder(expectedServiceOrderDTO)
//                .employees(expectedEmployees)
//                .build();
//
//        // Act
//        NotificationResponseDTO actualDTO = presenter.toNotificationResponseDTO(notification);
//
//        // Assert
//        assertEquals(expectedDTO, actualDTO);
//    }

}
