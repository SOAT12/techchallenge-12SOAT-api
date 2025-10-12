package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.*;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;

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

    public ServiceOrderEntity toServiceOrderEntity(ServiceOrder dto) {
        ServiceOrderEntity entity = ServiceOrderEntity.builder()
                .id(dto.getId())
                .finishedAt(dto.getFinishedAt())
                .status(dto.getStatus())
                .totalValue(dto.getTotalValue())
                .notes(dto.getNotes())
                .customer(customerMapper.toCustomerJpaEntity(dto.getCustomer()))
                .vehicle(vehicleMapper.toVehicleJpaEntity(dto.getVehicle()))
                .employee(employeeMapper.toEmployeeJpaEntity(dto.getEmployee()))
                .services(new HashSet<>())
                .stockItems(new HashSet<>())
                .build();

        if (dto.getServices() != null) {
            for (VehicleService service : dto.getServices()) {
                ServiceOrderVehicleServiceEntity serviceEntity = new ServiceOrderVehicleServiceEntity();

                ServiceOrderVehicleServiceIdEntity id = new ServiceOrderVehicleServiceIdEntity();
                id.setServiceOrderId(dto.getId());
                id.setVehicleServiceId(service.getId());

                serviceEntity.setId(id);
                serviceEntity.setServiceOrder(entity);
                serviceEntity.setVehicleService(vehicleServiceMapper.toVehicleServiceJpaEntity(service));

                entity.getServices().add(serviceEntity);
            }
        }

        if (dto.getStockItems() != null) {
            for (Stock stock : dto.getStockItems()) {
                ServiceOrderStockEntity stockEntity = new ServiceOrderStockEntity();

                ServiceOrderStockIdEntity id = new ServiceOrderStockIdEntity();
                id.setServiceOrderId(dto.getId());
                id.setStockId(stock.getId());

                stockEntity.setId(id);
                stockEntity.setServiceOrder(entity);
                stockEntity.setStock(stockMapper.toEntity(stock));

                entity.getStockItems().add(stockEntity);
            }
        }

        return entity;
    }

}
