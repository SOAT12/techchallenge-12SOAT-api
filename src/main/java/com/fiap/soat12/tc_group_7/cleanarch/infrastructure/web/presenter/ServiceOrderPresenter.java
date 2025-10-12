package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceOrderPresenter {

    public ServiceOrderResponseDTO toServiceOrderResponseDTO(ServiceOrder order) {
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
                                serviceItem.getName(),
                                serviceItem.getValue()
                        )
                )
                .collect(Collectors.toList());

        List<ServiceOrderResponseDTO.StockItemDetailDTO> stockItemsMap = order.getStockItems().stream().map(
                        stockItem -> new ServiceOrderResponseDTO.StockItemDetailDTO(
                                stockItem.getToolName(),
                                stockItem.getQuantity(),
                                stockItem.getValue()
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
