package com.fiap.soat12.tc_group_7.dto.notification;

import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;

    private String message;

    private Boolean isRead;

    private ServiceOrderDTO serviceOrder;

    private List<EmployeeDTO> employees;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceOrderDTO {
        private Long id;
        private Status status;
        private BigDecimal totalValue;
        private Date createdAt;
        private Date finishedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeDTO {
        private Long id;
        private String name;
    }

}
