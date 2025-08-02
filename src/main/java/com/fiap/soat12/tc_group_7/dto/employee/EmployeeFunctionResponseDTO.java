package com.fiap.soat12.tc_group_7.dto.employee;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFunctionResponseDTO {
    private Long id;
    private String description;
    private Boolean active;
}
