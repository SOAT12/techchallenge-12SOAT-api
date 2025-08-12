package com.fiap.soat12.tc_group_7.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AverageExecutionTimeResponseDTO {
    private Long averageExecutionTimeHours;
    private String averageExecutionTimeFormatted;
}
