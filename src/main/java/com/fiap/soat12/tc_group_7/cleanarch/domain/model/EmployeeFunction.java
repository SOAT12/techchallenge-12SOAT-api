package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmployeeFunction {

    private Long id;

    private String description;

    private Boolean active;

}
