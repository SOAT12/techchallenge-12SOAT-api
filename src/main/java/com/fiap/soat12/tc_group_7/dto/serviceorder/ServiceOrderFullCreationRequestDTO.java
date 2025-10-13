package com.fiap.soat12.tc_group_7.dto.serviceorder;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderFullCreationRequestDTO {

    private CustomerRequestDTO customer;

    private VehicleRequestDTO vehicle;

    private List<VehicleServiceRequestDTO> services;

    private List<StockRequestDTO> stockItems;

    private Long employeeId;

    private String notes;

}
