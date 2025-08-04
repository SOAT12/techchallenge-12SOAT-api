package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository repository;
    private final ServiceOrderRepository serviceOrderRepository;

    @Transactional
    public ServiceOrderResponseDTO createOrder(ServiceOrderRequestDTO request) {

        ServiceOrder order = new ServiceOrder();
        BeanUtils.copyProperties(request, order);

        order.setStatus(Status.OPENED); // Initial state
        ServiceOrder savedOrder = serviceOrderRepository.save(order);
        return convertToResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponseDTO findById(Long id) {
        return repository.findById(id).map(this::convertToResponseDTO)
                .orElseThrow(() -> new NotFoundException("Service Order not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ServiceOrderResponseDTO> findAllOrders() {
        return repository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteOrderLogically(Long id) {
        repository.findById(id)
                .map(order -> {
                    order.setActive(false);
                    repository.save(order);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        return false;
    }

    public Optional<ServiceOrderResponseDTO> updateOrder(Long id, ServiceOrderRequestDTO requestDTO) {
        return Optional.ofNullable(serviceOrderRepository.findById(id)
                .map(existingOrder -> {
                    BeanUtils.copyProperties(requestDTO, existingOrder);
                    existingOrder.setStatus(Status.OPENED);
                    serviceOrderRepository.save(existingOrder);

                    return convertToResponseDTO(existingOrder);
                }).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrado")));
    }

    private ServiceOrder getOrderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service Order not found with id: " + id));
    }

    @Transactional
    public ServiceOrderResponseDTO diagnose(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().diagnose(order);
        return convertToResponseDTO(repository.save(order));
    }

    @Transactional
    public ServiceOrderResponseDTO waitForApproval(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().waitForApproval(order);
        return convertToResponseDTO(repository.save(order));
    }

    @Transactional
    public ServiceOrderResponseDTO approve(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().approve(order);
        return convertToResponseDTO(repository.save(order));
    }

    @Transactional
    public ServiceOrderResponseDTO reject(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().reject(order);
        return convertToResponseDTO(repository.save(order));
    }

    private ServiceOrderResponseDTO convertToResponseDTO(ServiceOrder order) {
        ServiceOrderResponseDTO dto = new ServiceOrderResponseDTO();
        BeanUtils.copyProperties(order, dto);
        return dto;
    }
}
