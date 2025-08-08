package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.entity.*;
import com.fiap.soat12.tc_group_7.entity.VehicleService;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.*;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ServiceOrderService {

    @Autowired
    private final ServiceOrderRepository serviceOrderRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final VehicleRepository vehicleRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final VehicleServiceRepository serviceRepository;
    @Autowired
    private final StockRepository stockRepository;

    @Transactional
    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO request) {
        ServiceOrder order = new ServiceOrder();

        assert customerRepository != null;
        assert vehicleRepository != null;
        assert employeeRepository != null;
        assert serviceOrderRepository != null;
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new NotFoundException("Attendant not found"));

        order.setCustomer(customer);
        order.setVehicle(vehicle);
        order.setEmployee(employee);

        mapServicesDetail(request, order);
        mapStockItemsDetail(request, order);

        order.setNotes(request.getNotes());
        order.setStatus(Status.OPENED);
        order.setTotalValue(order.calculateTotalValue(order.getServices(), order.getStockItems()));

        ServiceOrder savedOrder = serviceOrderRepository.save(order);
        return toResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponseDTO findById(Long id) {
        return serviceOrderRepository.findById(id).map(this::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada com o id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ServiceOrderResponseDTO> findAllOrders() {
        return serviceOrderRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteOrderLogically(Long id) {
        serviceOrderRepository.findById(id)
                .map(order -> {
                    order.setStatus(Status.CANCELED);
                    serviceOrderRepository.save(order);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
        return false;
    }

    public Optional<ServiceOrderResponseDTO> updateOrder(Long id, ServiceOrderRequestDTO requestDTO) {
        return Optional.ofNullable(serviceOrderRepository.findById(id)
                .map(existingOrder -> {
                    BeanUtils.copyProperties(requestDTO, existingOrder);
                    existingOrder.setStatus(Status.WAITING_FOR_APPROVAL);
                    existingOrder.setUpdatedAt(new Date());

                    existingOrder.getServices().clear();
                    if (requestDTO.getServices() != null) {
                        requestDTO.getServices().forEach(serviceDto -> {
                            assert serviceRepository != null;
                            VehicleService serviceEntity = serviceRepository.findById(serviceDto.getServiceId()).orElseThrow(() -> new NotFoundException("Serviço não encontrado"));

                            ServiceOrderVehicleService newServiceLink = new ServiceOrderVehicleService();
                            newServiceLink.setServiceOrder(existingOrder);
                            newServiceLink.setVehicleService(serviceEntity);
                            newServiceLink.setId(new ServiceOrderVehicleServiceId(existingOrder.getId(), serviceEntity.getId()));

                            existingOrder.getServices().add(newServiceLink);
                        });
                    }

                    existingOrder.getStockItems().clear();
                    if (requestDTO.getStockItems() != null) {
                        requestDTO.getStockItems().forEach(itemsDTO -> {
                            assert serviceRepository != null;
                            Stock stockEntity = stockRepository.findById(itemsDTO.getStockId()).orElseThrow(() -> new NotFoundException("Peça não encontrada"));

                            ServiceOrderStock newItemLink = new ServiceOrderStock();
                            newItemLink.setServiceOrder(existingOrder);
                            newItemLink.setStock(stockEntity);
                            newItemLink.getStock().setQuantity(itemsDTO.getRequiredQuantity());
                            newItemLink.setId(new ServiceOrderStockId(existingOrder.getId(), stockEntity.getId()));

                            existingOrder.getStockItems().add(newItemLink);
                        });
                    }

                    existingOrder.setTotalValue(existingOrder.calculateTotalValue(existingOrder.getServices(), existingOrder.getStockItems()));

                    serviceOrderRepository.save(existingOrder);

                    return toResponseDTO(existingOrder);
                }).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrado")));
    }

    private ServiceOrder getOrderById(Long id) {
        return serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service Order not found with id: " + id));
    }

    @Transactional
    public ServiceOrderResponseDTO diagnose(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().diagnose(order);
        return toResponseDTO(serviceOrderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponseDTO waitForApproval(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().waitForApproval(order);
        return toResponseDTO(serviceOrderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponseDTO approve(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().approve(order);
        return toResponseDTO(serviceOrderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponseDTO reject(Long id, String reason) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().reject(order);
        order.setNotes(reason);
        return toResponseDTO(serviceOrderRepository.save(order));
    }

    private ServiceOrderResponseDTO toResponseDTO(ServiceOrder order) {
        if (order == null) {
            return null;
        }

        ServiceOrderResponseDTO.CustomerDTO customerDTO = new ServiceOrderResponseDTO.CustomerDTO(
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getCpf()
        );

        ServiceOrderResponseDTO.VehicleDTO vehicleDTO = new ServiceOrderResponseDTO.VehicleDTO(
                order.getVehicle().getId(),
                order.getVehicle().getLicensePlate(),
                order.getVehicle().getModel()
        );

        ServiceOrderResponseDTO.EmployeeDTO attendantDTO = new ServiceOrderResponseDTO.EmployeeDTO(
                order.getEmployee().getId(),
                order.getEmployee().getName()
        );

        List<ServiceOrderResponseDTO.ServiceItemDetailDTO> servicesMap = order.getServices().stream().map(
                        serviceItem -> new ServiceOrderResponseDTO.ServiceItemDetailDTO(
                                serviceItem.getVehicleService().getName(),
                                serviceItem.getVehicleService().getValue()
                        )
                )
                .collect(Collectors.toList());

        List<ServiceOrderResponseDTO.StockItemDetailDTO> stockItemsMap = order.getStockItems().stream().map(
                        stockItem -> new ServiceOrderResponseDTO.StockItemDetailDTO(
                                stockItem.getStock().getToolName(),
                                stockItem.getStock().getQuantity(),
                                stockItem.getStock().getValue()
                        )
                )
                .collect(Collectors.toList());

        ServiceOrderResponseDTO dto = new ServiceOrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setFinishedAt(order.getFinishedAt());

        dto.setTotalValue(order.getTotalValue());

        dto.setCustomer(customerDTO);
        dto.setVehicle(vehicleDTO);
        dto.setEmployee(attendantDTO);
        dto.setServices(servicesMap);
        dto.setStockItems(stockItemsMap);

        return dto;
    }

    private void mapServicesDetail(ServiceOrderRequestDTO request, ServiceOrder order) {
        if (request.getServices() != null) {
            order.setServices(request.getServices().stream().map(dto -> {
                ServiceOrderVehicleService sos = new ServiceOrderVehicleService();
                VehicleService service = serviceRepository.findById(dto.getServiceId()).orElseThrow(() -> new NotFoundException("Serviço não encontrado"));
                sos.setVehicleService(service);
                sos.setServiceOrder(order);
                sos.setId(new ServiceOrderVehicleServiceId(order.getId(), service.getId()));
                return sos;
            }).collect(Collectors.toSet()));
        }
    }

    private void mapStockItemsDetail(ServiceOrderRequestDTO request, ServiceOrder order) {
        if (request.getStockItems() != null) {
            order.setStockItems(request.getStockItems().stream().map(dto -> {
                Stock stock = stockRepository.findById(dto.getStockId()).orElseThrow(() -> new NotFoundException("Peça não encontrada"));
                ServiceOrderStock sos = new ServiceOrderStock();
                sos.setStock(stock);
                sos.getStock().setQuantity(dto.getRequiredQuantity());
                sos.setServiceOrder(order);
                sos.setId(new ServiceOrderStockId(order.getId(), stock.getId()));
                return sos;
            }).collect(Collectors.toSet()));
        }
    }
}
