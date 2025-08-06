package com.fiap.soat12.tc_group_7.dto;

import com.fiap.soat12.tc_group_7.entity.Stock;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderRequestDTO {
        
        @NotNull(message = "Customer ID cannot be null")
        private Integer customerId;

        @NotNull(message = "Vehicle ID cannot be null")
        private Integer vehicleId;

        @NotNull(message = "Employee ID cannot be null")
        private Integer employeeId;

//        private List<Object> vehicleServices;

        private List<Stock> stockItems;

//        @NotNull(message = "O valor não pode ser nulo.")
//        @Positive(message = "O valor deve ser maior que zero.")
//        private BigDecimal totalValue;

        private String notes;
}
