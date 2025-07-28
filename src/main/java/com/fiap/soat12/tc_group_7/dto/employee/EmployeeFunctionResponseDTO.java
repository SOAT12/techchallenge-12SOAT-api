package com.fiap.soat12.tc_group_7.dto.employee;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeFunctionResponseDTO {
    private Long id;
    private String description;
    private String createdAt;
    private String updatedAt;
}
