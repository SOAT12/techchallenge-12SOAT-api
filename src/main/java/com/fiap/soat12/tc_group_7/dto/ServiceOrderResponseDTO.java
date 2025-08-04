package com.fiap.soat12.tc_group_7.dto;

import com.fiap.soat12.tc_group_7.util.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderResponseDTO {
    
        private Long id;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private OffsetDateTime finishedAt;
        private Integer customerId;
        private Integer vehicleId;
        private Integer employeeId;
        private Status status;
        private BigDecimal totalValue;
        private String notes;
}
