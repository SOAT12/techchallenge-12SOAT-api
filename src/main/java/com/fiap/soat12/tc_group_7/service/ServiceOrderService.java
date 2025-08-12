package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.entity.ServiceOrderStock;
import com.fiap.soat12.tc_group_7.entity.ServiceOrderStockId;
import com.fiap.soat12.tc_group_7.entity.ServiceOrderVehicleService;
import com.fiap.soat12.tc_group_7.entity.ServiceOrderVehicleServiceId;
import com.fiap.soat12.tc_group_7.entity.Stock;
import com.fiap.soat12.tc_group_7.entity.Vehicle;
import com.fiap.soat12.tc_group_7.entity.VehicleService;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.CustomerRepository;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.repository.StockRepository;
import com.fiap.soat12.tc_group_7.repository.VehicleRepository;
import com.fiap.soat12.tc_group_7.repository.VehicleServiceRepository;
import com.fiap.soat12.tc_group_7.specification.ServiceOrderSpecification;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fiap.soat12.tc_group_7.util.Status.getStatusesForPendingOrders;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ServiceOrderService {

    protected static final String MECHANIC_DESCRIPTION = "Mecânico";

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
    @Autowired
    private final NotificationService notificationService;

    @Transactional
    public ServiceOrderResponseDTO createServiceOrder(ServiceOrderRequestDTO request) {
        ServiceOrder order = new ServiceOrder();

        assert customerRepository != null;
        assert vehicleRepository != null;
        assert employeeRepository != null;
        assert serviceOrderRepository != null;
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));

        Employee employee;
        if (nonNull(request.getEmployeeId())) {
            employee = employeeRepository.findById(request.getEmployeeId()).orElseThrow(() -> new NotFoundException("Employee not found"));
        } else {
            employee = this.findMostAvailableEmployee();
        }

        order.setCustomer(customer);
        order.setVehicle(vehicle);
        order.setEmployee(employee);

        mapServicesDetail(request, order);
        mapStockItemsDetail(request, order);

        order.setNotes(request.getNotes());
        order.setStatus(Status.OPENED);
        order.setTotalValue(order.calculateTotalValue(order.getServices(), order.getStockItems()));

        ServiceOrder savedOrder = serviceOrderRepository.save(order);
        notificationService.notifyMechanicAssignedToOS(savedOrder, employee);
        return toResponseDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponseDTO findById(Long id) {
        return serviceOrderRepository.findById(id).map(this::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada: " + id));
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
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada com o id: " + id));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> diagnose(Long id, Long employeeId) throws InvalidTransitionException {
        Employee employee = null;
        ServiceOrder order = getOrderById(id);

        if (employeeId != null) {
            employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Funcionário não encontrado."));
            order.setEmployee(employee);
        }

        order.getStatus().diagnose(order);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> waitForApproval(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().waitForApproval(order);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> approve(Long id, Long employeeId) throws InvalidTransitionException {
        Employee employee = null;
        ServiceOrder order = getOrderById(id);

        if (employeeId != null) {
            employee = employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Funcionário não encontrado."));
            order.setEmployee(employee);
        }

        notificationService.notifyMechanicOSApproved(order, order.getEmployee());

        order.getStatus().approve(order);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> reject(Long id, String reason) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().reject(order);
        order.setNotes(reason);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> waitForStock(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().waitForStock(order);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> execute(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().execute(order);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> finish(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().finish(order);
        notificationService.notifyAttendantsOSCompleted(order);
        //todo chamar serviço de envio de email ao cliente
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    @Transactional
    public Optional<ServiceOrderResponseDTO> deliver(Long id) throws InvalidTransitionException {
        ServiceOrder order = getOrderById(id);
        order.getStatus().deliver(order);
        return Optional.ofNullable(toResponseDTO(serviceOrderRepository.save(order)));
    }

    public AverageExecutionTimeResponseDTO calculateAverageExecutionTime(Date startDate, Date endDate, List<Long> serviceIds) {
        List<ServiceOrder> finishedOrders = serviceOrderRepository.findAll(
                ServiceOrderSpecification.withFilters(startDate, endDate, serviceIds)
        );

        if (finishedOrders.isEmpty()) {
            return new AverageExecutionTimeResponseDTO(0L, "0 horas, 0 minutos");
        }

        long totalMillis = finishedOrders.stream()
                .mapToLong(order -> {
                    Date startedAt = order.getCreatedAt();
                    Date finishedAt = order.getFinishedAt();
                    return finishedAt.getTime() - startedAt.getTime();
                })
                .sum();

        long avgMillis = totalMillis / finishedOrders.size();
        Duration avgDuration = Duration.ofMillis(avgMillis);

        return new AverageExecutionTimeResponseDTO(
                avgDuration.toHours(),
                String.format("%d horas, %d minutos", avgDuration.toHours(), avgDuration.toMinutesPart())
        );
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

    protected Employee findMostAvailableEmployee() {
        List<Status> activeStatuses = getStatusesForPendingOrders();

        List<Employee> activeEmployees = employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MECHANIC_DESCRIPTION);

        if (activeEmployees.isEmpty()) {
            throw new NotFoundException("Nenhum mecânico disponível");
        }

        return activeEmployees.stream()
                .min(Comparator
                        .comparingLong((Employee employee) -> serviceOrderRepository.countByEmployeeAndStatusIn(employee, activeStatuses))
                        .thenComparing(employee -> {
                            Date oldestOrderDate = findOldestOrderDateForEmployee(employee, activeStatuses);
                            return oldestOrderDate == null ? Long.MIN_VALUE : oldestOrderDate.getTime();
                        })
                )
                .orElseThrow(() -> new NotFoundException("Nenhum mecânico disponível"));
    }

    protected Date findOldestOrderDateForEmployee(Employee employee, List<Status> activeStatuses) {
        List<ServiceOrder> activeOrders = serviceOrderRepository.findByEmployeeAndStatusIn(employee, activeStatuses);
        return activeOrders.stream()
                .map(ServiceOrder::getCreatedAt)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    private ServiceOrderResponseDTO toResponseDTO(ServiceOrder order) {
        if (order == null) {
            return null;
        }

        ServiceOrderResponseDTO.CustomerDTO customerDTO = null;
        ServiceOrderResponseDTO.VehicleDTO vehicleDTO = null;
        ServiceOrderResponseDTO.EmployeeDTO attendantDTO = null;

        customerDTO = new ServiceOrderResponseDTO.CustomerDTO(
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getCpf()
        );

        vehicleDTO = new ServiceOrderResponseDTO.VehicleDTO(
                order.getVehicle().getId(),
                order.getVehicle().getLicensePlate(),
                order.getVehicle().getModel()
        );

        if (order.getEmployee() != null) {
            attendantDTO = new ServiceOrderResponseDTO.EmployeeDTO(
                    order.getEmployee().getId(),
                    order.getEmployee().getName()
            );
        }

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
}
