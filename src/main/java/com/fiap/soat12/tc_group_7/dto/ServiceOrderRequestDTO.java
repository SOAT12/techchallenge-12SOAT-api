package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ServiceOrderRequestDTO {
        
        @NotNull(message = "Customer ID cannot be null")
        private Integer customerId;

        @NotNull(message = "Vehicle ID cannot be null")
        private Integer vehicleId;

        @NotNull(message = "Employee ID cannot be null")
        private Integer employeeId;

        @NotNull(message = "O valor não pode ser nulo.")
        @Positive(message = "O valor deve ser maior que zero.")
        private BigDecimal totalValue;

        private String notes;
}
