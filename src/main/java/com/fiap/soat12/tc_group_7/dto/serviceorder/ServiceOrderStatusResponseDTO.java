package com.fiap.soat12.tc_group_7.dto.serviceorder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ServiceOrderStatusResponseDTO {

    private String status;

}
