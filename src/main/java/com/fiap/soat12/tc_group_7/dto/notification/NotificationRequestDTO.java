package com.fiap.soat12.tc_group_7.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    @NotBlank(message = "A mensagem não pode estar em branco")
    private String message;

    @NotNull(message = "O ID da ordem de serviço é obrigatório")
    private Long serviceOrderId;

    @NotNull(message = "A lista de funcionários não pode ser nula")
    private Set<Long> employeeIds;

}
