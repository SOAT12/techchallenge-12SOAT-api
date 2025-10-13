package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ServiceOrderUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ServiceOrderPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import com.fiap.soat12.tc_group_7.dto.serviceorder.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.serviceorder.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.serviceorder.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.dto.serviceorder.ServiceOrderStatusResponseDTO;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceOrderControllerTest {

    @Mock
    private ServiceOrderUseCase serviceOrderUseCase;

    @Mock
    private ServiceOrderPresenter serviceOrderPresenter;

    @InjectMocks
    private ServiceOrderController controller;

    @Test
    void createOrder_shouldReturnResponseDTO() {
        ServiceOrderRequestDTO requestDTO = new ServiceOrderRequestDTO();
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        ServiceOrderResponseDTO responseDTO = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.createServiceOrder(requestDTO)).thenReturn(serviceOrder);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder)).thenReturn(responseDTO);

        ServiceOrderResponseDTO result = controller.createOrder(requestDTO);

        assertEquals(responseDTO, result);
        verify(serviceOrderUseCase).createServiceOrder(requestDTO);
        verify(serviceOrderPresenter).toServiceOrderResponseDTO(serviceOrder);
    }

    @Test
    void findAllOrders_shouldReturnList() {
        List<ServiceOrder> orders = List.of(mock(ServiceOrder.class));
        List<ServiceOrderResponseDTO> expected = List.of(mock(ServiceOrderResponseDTO.class));

        when(serviceOrderUseCase.findAllOrders()).thenReturn(orders);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(any())).thenReturn(expected.get(0));

        List<ServiceOrderResponseDTO> result = controller.findAllOrders();

        assertEquals(expected, result);
        verify(serviceOrderUseCase).findAllOrders();
    }

    @Test
    void findOrderById_shouldReturnOrder() {
        Long id = 1L;
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        ServiceOrderResponseDTO responseDTO = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.findById(id)).thenReturn(serviceOrder);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder)).thenReturn(responseDTO);

        ServiceOrderResponseDTO result = controller.findOrderById(id);

        assertEquals(responseDTO, result);
    }

    @Test
    void findByCustomerInfo_shouldReturnList() {
        String document = "123456789";
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.findByCustomerInfo(document)).thenReturn(List.of(serviceOrder));
        when(serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder)).thenReturn(dto);

        List<ServiceOrderResponseDTO> result = controller.findByCustomerInfo(document);

        assertEquals(List.of(dto), result);
    }

    @Test
    void findByVehicleInfo_shouldReturnList() {
        String plate = "ABC1234";
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.findByVehicleInfo(plate)).thenReturn(List.of(serviceOrder));
        when(serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder)).thenReturn(dto);

        List<ServiceOrderResponseDTO> result = controller.findByVehicleInfo(plate);

        assertEquals(List.of(dto), result);
    }

    @Test
    void updateServiceOrder_shouldReturnUpdatedDTO() {
        Long id = 10L;
        ServiceOrderRequestDTO dto = new ServiceOrderRequestDTO();
        ServiceOrder updatedOrder = mock(ServiceOrder.class);
        ServiceOrderResponseDTO response = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.updateOrder(id, dto)).thenReturn(updatedOrder);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(updatedOrder)).thenReturn(response);

        ServiceOrderResponseDTO result = controller.updateServiceOrder(id, dto);

        assertEquals(response, result);
    }

    @Test
    void deleteOrderLogically_shouldCallUseCase() {
        Long id = 15L;

        controller.deleteOrderLogically(id);

        verify(serviceOrderUseCase).deleteOrderLogically(id);
    }

    @Test
    void diagnose_shouldReturnDiagnosedOrder() throws InvalidTransitionException {
        Long id = 1L, employeeId = 2L;
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.diagnose(id, employeeId)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.diagnose(id, employeeId);

        assertEquals(dto, result);
    }

    @Test
    void waitForApproval_shouldReturnDTO() throws InvalidTransitionException, MessagingException {
        Long id = 1L;
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.waitForApproval(id)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.waitForApproval(id);

        assertEquals(dto, result);
    }

    @Test
    void approve_shouldReturnDTO() throws InvalidTransitionException {
        Long id = 1L, empId = 2L;
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.approve(id, empId)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.approve(id, empId);

        assertEquals(dto, result);
    }

    @Test
    void reject_shouldReturnDTO() throws InvalidTransitionException {
        Long id = 1L;
        String reason = "Incompleto";
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.reject(id, reason)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.reject(id, reason);

        assertEquals(dto, result);
    }

    @Test
    void startOrderExecution_shouldReturnDTO() {
        Long id = 1L;
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.startOrderExecution(id)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.startOrderExecution(id);

        assertEquals(dto, result);
    }

    @Test
    void finish_shouldReturnDTO() throws InvalidTransitionException, MessagingException {
        Long id = 1L;
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.finish(id)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.finish(id);

        assertEquals(dto, result);
    }

    @Test
    void deliver_shouldReturnDTO() throws InvalidTransitionException {
        Long id = 1L;
        ServiceOrder order = mock(ServiceOrder.class);
        ServiceOrderResponseDTO dto = mock(ServiceOrderResponseDTO.class);

        when(serviceOrderUseCase.deliver(id)).thenReturn(order);
        when(serviceOrderPresenter.toServiceOrderResponseDTO(order)).thenReturn(dto);

        ServiceOrderResponseDTO result = controller.deliver(id);

        assertEquals(dto, result);
    }

    @Test
    void calculateAverageExecutionTime_shouldReturnFormattedDTO() {
        Date start = new Date();
        Date end = new Date();
        List<Long> serviceIds = List.of(1L, 2L);

        Duration duration = Duration.ofHours(2).plusMinutes(30);
        AverageExecutionTimeResponseDTO expected = new AverageExecutionTimeResponseDTO(2L, "2 horas, 30 minutos");

        when(serviceOrderUseCase.calculateAverageExecutionTime(start, end, serviceIds)).thenReturn(duration);
        when(serviceOrderPresenter.toAverageExecutionTimeResponseDTO(duration)).thenReturn(expected);

        AverageExecutionTimeResponseDTO result = controller.calculateAverageExecutionTime(start, end, serviceIds);

        assertEquals(expected, result);
    }

    @Test
    void getServiceOrderStatus_shouldReturnStatusDTO() {
        Long id = 1L;
        ServiceOrder order = mock(ServiceOrder.class);
        Status status = Status.OPENED;
        ServiceOrderStatusResponseDTO dto = new ServiceOrderStatusResponseDTO(status.getLabel());

        when(serviceOrderUseCase.findById(id)).thenReturn(order);
        when(order.getStatus()).thenReturn(status);
        when(serviceOrderPresenter.toServiceOrderStatusResponseDTO(status)).thenReturn(dto);

        ServiceOrderStatusResponseDTO result = controller.getServiceOrderStatus(id);

        assertEquals(dto, result);
    }


    @Test
    void approval_shouldCallUseCaseWithCorrectParameters() {
        // Arrange
        Long id = 1L;
        Boolean approval = true;

        // Act
        controller.approval(id, approval);

        // Assert
        verify(serviceOrderUseCase, times(1)).approval(id, approval);
    }

}
