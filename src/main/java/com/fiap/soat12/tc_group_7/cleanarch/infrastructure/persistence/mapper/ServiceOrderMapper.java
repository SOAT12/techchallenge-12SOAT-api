package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.*;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ServiceOrderMapper {

    private final CustomerMapper customerMapper;
    private final VehicleMapper vehicleMapper;
    private final EmployeeMapper employeeMapper;
    private final VehicleServiceMapper vehicleServiceMapper;
    private final StockMapper stockMapper;

    public ServiceOrder toServiceOrder(ServiceOrderEntity entity) {
        ServiceOrder serviceOrder = ServiceOrder.builder()
                .id(entity.getId())
                .finishedAt(entity.getFinishedAt())
                .status(entity.getStatus())
                .totalValue(entity.getTotalValue())
                .notes(entity.getNotes())
                .customer(customerMapper.toCustomer(entity.getCustomer()))
                .vehicle(vehicleMapper.toVehicle(entity.getVehicle()))
                .employee(employeeMapper.toEmployee(entity.getEmployee()))
                .services(new HashSet<>())
                .stockItems(new HashSet<>())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        if (entity.getServices() != null) {
            for (ServiceOrderVehicleServiceEntity serviceEntity : entity.getServices()) {
                VehicleService service = vehicleServiceMapper.toVehicleService(serviceEntity.getVehicleService());
                serviceOrder.getServices().add(service);
            }
        }

        if (entity.getStockItems() != null) {
            for (ServiceOrderStockEntity stockEntity : entity.getStockItems()) {
                Stock stock = stockMapper.toDomain(stockEntity.getStock());
                serviceOrder.getStockItems().add(stock);
            }
        }

        return serviceOrder;
    }

    public ServiceOrderEntity toServiceOrderEntity(ServiceOrder serviceOrder,
                                                   CustomerJpaEntity customer,
                                                   VehicleJpaEntity vehicle,
                                                   EmployeeJpaEntity employee,
                                                   Set<ServiceOrderVehicleServiceEntity> services,
                                                   Set<ServiceOrderStockEntity> stockItems) {

        ServiceOrderEntity entity = ServiceOrderEntity.builder()
                .id(serviceOrder.getId())
                .finishedAt(serviceOrder.getFinishedAt())
                .status(serviceOrder.getStatus())
                .totalValue(serviceOrder.getTotalValue())
                .notes(serviceOrder.getNotes())
                .customer(customer)
                .vehicle(vehicle)
                .employee(employee)
                .services(new HashSet<>())
                .stockItems(new HashSet<>())
                .build();

        for (var service : services) {
            service.setServiceOrder(entity);
            service.getId().setServiceOrderId(serviceOrder.getId());
            entity.getServices().add(service);
        }

        for (var stock : stockItems) {
            stock.setServiceOrder(entity);
            stock.getId().setServiceOrderId(serviceOrder.getId());
            entity.getStockItems().add(stock);
        }

        return entity;
    }

}
