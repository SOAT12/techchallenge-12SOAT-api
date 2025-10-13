package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ServiceOrderUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ServiceOrderPresenter;
import com.fiap.soat12.tc_group_7.dto.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderUseCase serviceOrderUseCase;
    private final ServiceOrderPresenter serviceOrderPresenter;

    public ServiceOrderResponseDTO createOrder(ServiceOrderRequestDTO requestDTO) {
        var serviceOrder = serviceOrderUseCase.createServiceOrder(requestDTO);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public List<ServiceOrderResponseDTO> findAllOrders() {
        return serviceOrderUseCase.findAllOrders().stream()
                .map(serviceOrderPresenter::toServiceOrderResponseDTO)
                .toList();
    }

    public ServiceOrderResponseDTO findOrderById(Long id) {
        var serviceOrder = serviceOrderUseCase.findById(id);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public List<ServiceOrderResponseDTO> findByCustomerInfo(String document) {
        return serviceOrderUseCase.findByCustomerInfo(document).stream()
                .map(serviceOrderPresenter::toServiceOrderResponseDTO)
                .toList();
    }

    public List<ServiceOrderResponseDTO> findByVehicleInfo(String licensePlate) {
        return serviceOrderUseCase.findByVehicleInfo(licensePlate).stream()
                .map(serviceOrderPresenter::toServiceOrderResponseDTO)
                .toList();
    }

    public ServiceOrderResponseDTO updateServiceOrder(Long id, ServiceOrderRequestDTO requestDTO) {
        var serviceOrder = serviceOrderUseCase.updateOrder(id, requestDTO);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public void deleteOrderLogically(Long id) {
        serviceOrderUseCase.deleteOrderLogically(id);
    }

    public ServiceOrderResponseDTO diagnose(Long id, Long employeeId) throws InvalidTransitionException {
        var serviceOrder = serviceOrderUseCase.diagnose(id, employeeId);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public ServiceOrderResponseDTO waitForApproval(Long id) throws InvalidTransitionException, MessagingException {
        var serviceOrder = serviceOrderUseCase.waitForApproval(id);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public ServiceOrderResponseDTO approve(Long id, Long employeeId) throws InvalidTransitionException {
        var serviceOrder = serviceOrderUseCase.approve(id, employeeId);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public ServiceOrderResponseDTO reject(Long id, String reason) throws InvalidTransitionException {
        var serviceOrder = serviceOrderUseCase.reject(id, reason);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public ServiceOrderResponseDTO startOrderExecution(Long serviceOrderId) {
        var serviceOrder = serviceOrderUseCase.startOrderExecution(serviceOrderId);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public ServiceOrderResponseDTO finish(Long id) throws InvalidTransitionException, MessagingException {
        var serviceOrder = serviceOrderUseCase.finish(id);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public ServiceOrderResponseDTO deliver(Long id) throws InvalidTransitionException {
        var serviceOrder = serviceOrderUseCase.deliver(id);
        return serviceOrderPresenter.toServiceOrderResponseDTO(serviceOrder);
    }

    public AverageExecutionTimeResponseDTO calculateAverageExecutionTime(Date startDate, Date endDate, List<Long> serviceIds) {
        Duration duration = serviceOrderUseCase.calculateAverageExecutionTime(startDate, endDate, serviceIds);
        return serviceOrderPresenter.toAverageExecutionTimeResponseDTO(duration);
    }

}
