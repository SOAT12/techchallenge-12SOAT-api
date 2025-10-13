package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.*;
import com.fiap.soat12.tc_group_7.cleanarch.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ServiceOrderGateway;
import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import com.fiap.soat12.tc_group_7.dto.serviceorder.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.stock.StockAvailabilityResponseDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.Duration;
import java.util.*;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class ServiceOrderUseCase {

    protected static final String FUNCTION_MECHANIC_DESCRIPTION = "Mecânico";

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
        } else {
            employee = this.findMostAvailableEmployee();
        }

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

    public List<ServiceOrder> findByVehicleInfo(String licensePlate) {
        Vehicle vehicle = vehicleUseCase.getVehicleByLicensePlate(licensePlate);
        return serviceOrderGateway.findByVehicleAndFinishedAtIsNull(vehicle);
    }

    public ServiceOrder updateOrder(Long id, ServiceOrderRequestDTO requestDTO) {
        return serviceOrderGateway.findById(id)
                .map(existingOrder -> {
                    BeanUtils.copyProperties(requestDTO, existingOrder);
                    existingOrder.setStatus(Status.WAITING_FOR_APPROVAL);
                    existingOrder.setUpdatedAt(new Date());

                    existingOrder.getServices().clear();
                    if (requestDTO.getServices() != null) {
                        requestDTO.getServices().forEach(serviceDto -> {
                            VehicleService service = vehicleServiceUseCase.getById(serviceDto.getServiceId());
                            existingOrder.getServices().add(service);
                        });
                    }

                    existingOrder.getStockItems().clear();
                    if (requestDTO.getStockItems() != null) {
                        requestDTO.getStockItems().forEach(itemsDTO -> {
                            Stock stock = stockUseCase.findStockItemById(itemsDTO.getStockId());
                            existingOrder.getStockItems().add(stock);
                        });
                    }

                    stockUseCase.checkStockAvailability(existingOrder);
                    existingOrder.setTotalValue(existingOrder.calculateTotalValue(existingOrder.getServices(), existingOrder.getStockItems()));

                    return serviceOrderGateway.save(existingOrder);
                }).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrado"));
    }

    public void deleteOrderLogically(Long id) {
        serviceOrderGateway.findById(id)
                .map(order -> {
                    order.setStatus(Status.CANCELED);
                    serviceOrderGateway.save(order);
                    return null;
                }).orElseThrow(() -> new NotFoundException("Veículo não encontrado"));
    }

    public ServiceOrder diagnose(Long id, Long employeeId) throws InvalidTransitionException {
        Employee employee = null;
        ServiceOrder order = findById(id);

        if (employeeId != null) {
            employee = employeeUseCase.getEmployeeById(employeeId);
            order.setEmployee(employee);
        } else {
            Employee availableEmployee = this.findMostAvailableEmployee();
            order.setEmployee(availableEmployee);
        }

        order.getStatus().diagnose(order);
        return serviceOrderGateway.save(order);
    }

    public ServiceOrder waitForApproval(Long id) throws InvalidTransitionException, MessagingException {
        final String BASE_URL = "http://localhost:8080/api/service-orders/";
        ServiceOrder order = findById(id);
        order.getStatus().waitForApproval(order);
        ServiceOrder serviceOrder = serviceOrderGateway.save(order);

        Map<String, Object> variables = new HashMap<>();
        variables.put("totalValue", order.getTotalValue());
        variables.put("userName", order.getCustomer().getName());
        variables.put("services", serviceOrder.getServices());
        variables.put("directApproveLink", BASE_URL + id + "/approve");
        variables.put("directRejectLink", BASE_URL + id + "/reject");

        String subject = order.getCustomer().getName() + " Seu Orçamento de Serviços está Pronto! (Aprovação Necessária)";

        //emailClient.sendMail(order.getCustomer().getEmail(), subject, "mailTemplateServices", variables);

        return serviceOrder;
    }

    public ServiceOrder approve(Long id, Long employeeId) throws InvalidTransitionException {
        Employee employee = null;
        ServiceOrder order = findById(id);

        if (employeeId != null) {
            employee = employeeUseCase.getEmployeeById(employeeId);
            order.setEmployee(employee);
        }

        notificationUseCase.notifyMechanicOSApproved(order, order.getEmployee());

        order.getStatus().approve(order);
        return serviceOrderGateway.save(order);
    }

    public ServiceOrder reject(Long id, String reason) throws InvalidTransitionException {
        ServiceOrder order = findById(id);
        order.getStatus().reject(order);
        order.setNotes(reason);
        return serviceOrderGateway.save(order);
    }

    public ServiceOrder startOrderExecution(Long serviceOrderId) {
        ServiceOrder order = findById(serviceOrderId);

        StockAvailabilityResponseDTO availability = stockUseCase.getStockAvailability(order);

        if (!availability.isAvailable()) {
            order.getStatus().waitForStock(order);
            notificationUseCase.notifyManagersOutOfStock(order);
        } else {
            order.getStatus().execute(order);
            Employee employee = this.findMostAvailableEmployee();
            order.setEmployee(employee);
            notificationUseCase.notifyMechanicAssignedToOS(order, employee);
        }

        return serviceOrderGateway.save(order);
    }

    public ServiceOrder finish(Long id) throws InvalidTransitionException, MessagingException {
        ServiceOrder order = findById(id);
        order.getStatus().finish(order);
        notificationUseCase.notifyAttendantsOSCompleted(order);
        ServiceOrder serviceOrder = serviceOrderGateway.save(order);

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", order.getId());
        variables.put("totalValue", order.getTotalValue());
        variables.put("userName", order.getCustomer().getName());
        variables.put("services", serviceOrder.getServices());
        variables.put("vehicle", serviceOrder.getVehicle());

        String subject = order.getCustomer().getName() + " Seus serviços solicitados foram finalizados";

        //mailClient.sendMail(order.getCustomer().getEmail(), subject, "mailTemplateServiceFinish", variables);

        return serviceOrder;
    }

    public ServiceOrder deliver(Long id) throws InvalidTransitionException {
        ServiceOrder order = findById(id);
        order.getStatus().deliver(order);
        return serviceOrderGateway.save(order);
    }

    public Duration calculateAverageExecutionTime(Date startDate, Date endDate, List<Long> serviceIds) {
        List<ServiceOrder> finishedOrders = serviceOrderGateway.findAllWithFilters(startDate, endDate, serviceIds);

        if (finishedOrders.isEmpty()) {
            return Duration.ZERO;
        }

        long totalMillis = finishedOrders.stream()
                .mapToLong(order -> {
                    Date startedAt = order.getCreatedAt();
                    Date finishedAt = order.getFinishedAt();
                    return finishedAt.getTime() - startedAt.getTime();
                })
                .sum();

        long avgMillis = totalMillis / finishedOrders.size();
        return Duration.ofMillis(avgMillis);
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
                        stockUseCase.updateStockItem(stock.getId(),
                                stock.getToolName(),
                                stock.getValue(),
                                stock.getQuantity(),
                                stock.isActive(),
                                stock.getToolCategory().getId());
                        order.getStockItems().add(stock);
                    });
        }
    }

    protected Employee findMostAvailableEmployee() {
        List<Status> activeStatuses = Status.getStatusesForPendingOrders();

        List<Employee> activeEmployees = employeeUseCase.getByEmployeeFunction(FUNCTION_MECHANIC_DESCRIPTION);

        if (activeEmployees.isEmpty()) {
            throw new NotFoundException("Nenhum mecânico disponível");
        }

        return activeEmployees.stream()
                .min(Comparator
                        .comparingLong((Employee employee) -> serviceOrderGateway.countByEmployeeAndStatusIn(employee, activeStatuses))
                        .thenComparing(employee -> {
                            Date oldestOrderDate = findOldestOrderDateForEmployee(employee, activeStatuses);
                            return oldestOrderDate == null ? Long.MIN_VALUE : oldestOrderDate.getTime();
                        })
                )
                .orElseThrow(() -> new NotFoundException("Nenhum mecânico disponível"));
    }

    protected Date findOldestOrderDateForEmployee(Employee employee, List<Status> activeStatuses) {
        List<ServiceOrder> activeOrders = serviceOrderGateway.findByEmployeeAndStatusIn(employee, activeStatuses);
        return activeOrders.stream()
                .map(ServiceOrder::getCreatedAt)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

}
