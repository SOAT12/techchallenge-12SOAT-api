package com.fiap.soat12.tc_group_7.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private String cpf;
    private String name;
    private String phone;
    private String email;
    private String city;
    private String state;
    private String district;
    private String street;
    private String number;
}
