package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.*;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ServiceOrderGateway;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class ServiceOrderUseCase {

    private final ServiceOrderGateway serviceOrderGateway;
    private final EmployeeUseCase employeeUseCase;
    private final CustomerUseCase customerUseCase;
    private final NotificationUseCase notificationUseCase;
    private final VehicleUseCase vehicleUseCase;
    private final VehicleServiceUseCase vehicleServiceUseCase;
    private final StockUseCase stockUseCase;

    public ServiceOrder createServiceOrder(ServiceOrderRequestDTO requestDTO) {
        ServiceOrder serviceOrder = new ServiceOrder();

        Customer customer = customerUseCase.getCustomerById(requestDTO.getCustomerId());
        Vehicle vehicle = vehicleUseCase.getVehicleById(requestDTO.getVehicleId());

        Employee employee = null;
        if (nonNull(requestDTO.getEmployeeId())) {
            employee = employeeUseCase.getEmployeeById(requestDTO.getEmployeeId());
        }
//        } else {
//            employee = this.findMostAvailableEmployee();
//        }

        serviceOrder.setCustomer(customer);
        serviceOrder.setVehicle(vehicle);
        serviceOrder.setEmployee(employee);

        mapServicesDetail(requestDTO, serviceOrder);
        mapStockItemsDetail(requestDTO, serviceOrder);

        serviceOrder.setNotes(requestDTO.getNotes());
        serviceOrder.setStatus(Status.OPENED);
        serviceOrder.setTotalValue(serviceOrder.calculateTotalValue(serviceOrder.getServices(), serviceOrder.getStockItems()));

        ServiceOrder savedOrder = serviceOrderGateway.save(serviceOrder);
        notificationUseCase.notifyMechanicAssignedToOS(savedOrder, employee);
        return savedOrder;
    }

    public ServiceOrder findById(Long id) {
        return serviceOrderGateway.findById(id)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada: " + id));
    }

    public List<ServiceOrder> findAllOrders() {
        return serviceOrderGateway.findAll();
    }

    public List<ServiceOrder> findByCustomerInfo(String document) {
        Customer customer = customerUseCase.getCustomerByCpf(document);

        return serviceOrderGateway.findByCustomerAndFinishedAtIsNull(customer);
    }

    public Optional<ServiceOrder> findByVehicleInfo(String licensePlate) {
        Vehicle vehicle = vehicleUseCase.getVehicleByLicensePlate(licensePlate);

        return Optional.ofNullable(serviceOrderGateway.findByVehicleAndFinishedAtIsNull(vehicle));
    }

    public boolean deleteOrderLogically(Long id) {
        serviceOrderGateway.findById(id)
                .map(order -> {
                    order.setStatus(Status.CANCELED);
                    serviceOrderGateway.save(order);
                    return true;
                }).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));

        return false;
    }

    private void mapServicesDetail(ServiceOrderRequestDTO request, ServiceOrder order) {
        if (request.getServices() != null) {
            request.getServices()
                    .forEach(dto -> {
                        VehicleService vehicleService = vehicleServiceUseCase.getById(dto.getServiceId());
                        order.getServices().add(vehicleService);
                    });
        }
    }

    private void mapStockItemsDetail(ServiceOrderRequestDTO request, ServiceOrder order) {
        if (request.getStockItems() != null) {
            request.getStockItems()
                    .forEach(dto -> {
                        Stock stock = stockUseCase.findStockItemById(dto.getStockId());
                        stock.setQuantity(stock.getQuantity() - dto.getRequiredQuantity());
                        order.getStockItems().add(stock);
                    });
        }
    }

}
