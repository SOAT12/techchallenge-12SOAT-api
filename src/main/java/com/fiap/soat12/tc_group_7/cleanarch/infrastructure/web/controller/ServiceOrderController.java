package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ServiceOrderUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ServiceOrderPresenter;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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

    public Optional<ServiceOrderResponseDTO> findByVehicleInfo(String licensePlate) {
        return serviceOrderUseCase.findByVehicleInfo(licensePlate)
                .map(serviceOrderPresenter::toServiceOrderResponseDTO);
    }

    public boolean deleteOrderLogically(Long id) {
        return serviceOrderUseCase.deleteOrderLogically(id);
    }
}
