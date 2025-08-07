package com.fiap.soat12.tc_group_7.dto;

import com.fiap.soat12.tc_group_7.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderResponseDTO {

        private Long id;
        private Status status;
        private BigDecimal totalValue;
        private String notes;
        private Date createdAt;
        private Date updatedAt;
        private Date finishedAt;
        private CustomerDTO customer;
        private VehicleDTO vehicle;
        private EmployeeDTO employee;

        private Map<Long, ServiceItemDetailDTO> services;

        private Map<Long, StockItemDetailDTO> stockItems;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CustomerDTO {
                private Long id;
                private String name;
                private String document;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class VehicleDTO {
                private Long id;
                private String licensePlate;
                private String model;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class EmployeeDTO {
                private Long id;
                private String name;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ServiceItemDetailDTO {
                private String name;
                private BigDecimal value;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class StockItemDetailDTO {
                private String toolName;
                private int quantity;
                private BigDecimal value;
        }
}