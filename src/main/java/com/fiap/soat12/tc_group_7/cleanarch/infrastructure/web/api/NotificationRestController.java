package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.NotificationController;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificação", description = "API para gerenciar notificações")
public class NotificationRestController {

    private final NotificationController notificationController;

    @GetMapping
    @Operation(summary = "Lista todas as notificações",
            description = "Retorna uma lista de todas as notificações cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso")
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationController.getAllNotifications();
    }

    @GetMapping("/by-employee")
    @Operation(summary = "Lista todas as notificações do funcionário",
            description = "Retorna uma lista de todas as notificações do funcionário.")
    @ApiResponse(responseCode = "200", description = "Lista de notificações retornada com sucesso")
    public List<NotificationResponseDTO> getNotificationsByEmployeeId(@RequestParam Long employeeId) {
        return notificationController.getNotificationsByEmployeeId(employeeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova notificação",
            description = "Registra uma nova notificação na base de dados.")
    @ApiResponse(responseCode = "201", description = "Notificação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public NotificationResponseDTO createNotification(@RequestBody @Valid NotificationRequestDTO requestDTO) {
        return notificationController.createNotification(requestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deleta uma notificação pelo ID",
            description = "Deleta a notificação no banco de dados."
    )
    @ApiResponse(responseCode = "204", description = "Notificação deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    public void deleteNotification(@PathVariable Long id) {
        notificationController.deleteNotification(id);
    }

}
