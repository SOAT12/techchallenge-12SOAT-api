package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.entity.*;
import com.fiap.soat12.tc_group_7.entity.VehicleService;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.*;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleServiceRepository serviceRepository;
    private final StockRepository stockRepository;

    @Transactional
    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO request) {
        ServiceOrder order = new ServiceOrder();

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new NotFoundException("Attendant not found"));

        order.setCustomer(customer);
        order.setVehicle(vehicle);
        order.setEmployee(employee);
        order.setNotes(request.getNotes());
        order.setStatus(Status.OPENED);

        if (request.getServices() != null) {
            order.setServices(request.getServices().stream().map(dto -> {
                ServiceOrderVehicleService sos = new ServiceOrderVehicleService();
                VehicleService service = serviceRepository.findById(dto.getServiceId()).orElseThrow(() -> new NotFoundException("Service not found"));
                sos.setVehicleService(service);
                sos.setServiceOrder(order);
                sos.setId(new ServiceOrderVehicleServiceId(order.getId(), service.getId()));
                return sos;
            }).collect(Collectors.toSet()));
        }

        if (request.getStockItems() != null) {
            order.setStockItems(request.getStockItems().stream().map(dto -> {
                ServiceOrderStock sos = new ServiceOrderStock();
                Stock stock = stockRepository.findById(dto.getStockId()).orElseThrow(() -> new NotFoundException("Stock item not found"));
                sos.setStock(stock);
                sos.getStock().setQuantity(dto.getRequiredQuantity());
                sos.setServiceOrder(order);
                sos.setId(new ServiceOrderStockId(order.getId(), stock.getId()));
                return sos;
            }).collect(Collectors.toSet()));
        }

        order.setTotalValue(order.calculateTotalValue(order.getServices(), order.getStockItems()));

        ServiceOrder savedOrder = serviceOrderRepository.save(order);
        return toResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponseDTO findById(Long id) {
        return serviceOrderRepository.findById(id).map(this::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Service Order not found with id: " + id));
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
                    existingOrder.setStatus(Status.OPENED);
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
    public ServiceOrderResponseDTO reject(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().reject(order);
        return toResponseDTO(serviceOrderRepository.save(order));
    }

    private ServiceOrderResponseDTO toResponseDTO(ServiceOrder order) {
        if (order == null) {
            return null;
        }

        // Create Customer DTO
        ServiceOrderResponseDTO.CustomerDTO customerDTO = new ServiceOrderResponseDTO.CustomerDTO(
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getCpf() // Assuming 'cpf' is the document
        );

        // Create Vehicle DTO
        ServiceOrderResponseDTO.VehicleDTO vehicleDTO = new ServiceOrderResponseDTO.VehicleDTO(
                order.getVehicle().getId(),
                order.getVehicle().getLicensePlate(),
                order.getVehicle().getModel()
        );

        // Create Attendant Employee DTO
        ServiceOrderResponseDTO.EmployeeDTO attendantDTO = new ServiceOrderResponseDTO.EmployeeDTO(
                order.getEmployee().getId(),
                order.getEmployee().getName()
        );

        // --- Map Collections to Maps ---

        // Create Services Map: Key is serviceId, Value is the detail DTO
        Map<Long, ServiceOrderResponseDTO.ServiceItemDetailDTO> servicesMap = order.getServices().stream()
                .collect(Collectors.toMap(
                        serviceItem -> serviceItem.getVehicleService().getId(),
                        serviceItem -> new ServiceOrderResponseDTO.ServiceItemDetailDTO(
                                serviceItem.getVehicleService().getName(),
                                serviceItem.getVehicleService().getValue()
                        )
                ));

        // Create Stock Items Map: Key is stockId, Value is the detail DTO
        Map<Long, ServiceOrderResponseDTO.StockItemDetailDTO> stockItemsMap = order.getStockItems().stream()
                .collect(Collectors.toMap(
                        stockItem -> stockItem.getStock().getId(),
                        stockItem -> new ServiceOrderResponseDTO.StockItemDetailDTO(
                                stockItem.getStock().getToolName(),
                                stockItem.getStock().getQuantity(),
                                stockItem.getStock().getValue()
                        )
                ));

        // --- Assemble the Main DTO ---

        ServiceOrderResponseDTO dto = new ServiceOrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setFinishedAt(order.getFinishedAt());

        // Set the calculated total value
        dto.setTotalValue(order.getTotalValue());

        // Set the nested objects and maps
        dto.setCustomer(customerDTO);
        dto.setVehicle(vehicleDTO);
        dto.setEmployee(attendantDTO);
        dto.setServices(servicesMap);
        dto.setStockItems(stockItemsMap);

        return dto;
    }
}
