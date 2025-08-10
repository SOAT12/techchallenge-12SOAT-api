package com.fiap.soat12.tc_group_7.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFunctionResponseDTO {
    private Long id;
    private String description;
    private Boolean active;
}
