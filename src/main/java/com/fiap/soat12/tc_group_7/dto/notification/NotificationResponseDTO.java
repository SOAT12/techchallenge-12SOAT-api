package com.fiap.soat12.tc_group_7.dto.notification;

import com.fiap.soat12.tc_group_7.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;

    private String message;

    private Boolean isRead;

    private ServiceOrderDTO serviceOrder;

    private List<EmployeeDTO> employees;

    @Getter
    @Setter
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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeDTO {
        private Long id;
        private String name;
    }

}
