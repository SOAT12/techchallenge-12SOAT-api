package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ServiceOrderController;
import com.fiap.soat12.tc_group_7.dto.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/service-orders")
@Tag(name = "Ordem de Serviço", description = "API para gerenciar ordem de serviço")
public class ServiceOrderApi {

    private final ServiceOrderController serviceOrderController;

    public ServiceOrderApi(ServiceOrderController serviceOrderController) {
        this.serviceOrderController = serviceOrderController;
    }

    @Operation(summary = "Cria uma nova ordem de serviço",
            description = "Cria uma nova ordem de serviço com base nos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Ordem de serviço criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public ServiceOrderResponseDTO createOrder(@Valid @RequestBody ServiceOrderRequestDTO request) {
        return serviceOrderController.createOrder(request);
    }

    @Operation(summary = "Obtém uma ordem de serviço pelo ID",
            description = "Retorna uma ordem de serviço específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @GetMapping("/{id}")
    public ServiceOrderResponseDTO getOrderById(@PathVariable Long id) {
        return serviceOrderController.findOrderById(id);
    }

    @Operation(summary = "Lista todas as ordens de serviço",
            description = "Retorna uma lista de todas as ordens de serviço cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista das ordens de serviço retornada com sucesso")
    @GetMapping
    public List<ServiceOrderResponseDTO> getAllOrders() {
        return serviceOrderController.findAllOrders();
    }

    @Operation(summary = "Lista ordens de serviço por CPF ou Placa",
            description = "Retorna ordens de serviço cadastradas não finalizadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordens de serviço retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Informar pelo menos um parâmetro para a busca."),
            @ApiResponse(responseCode = "404", description = "Nenhuma ordem de serviço encontrada para os parâmetros informados.")
    })
    @GetMapping("/consult")
    public ResponseEntity<List<ServiceOrderResponseDTO>> consultOrder(
            @RequestParam(required = false) String document,
            @RequestParam(required = false) String licensePlate) {

//        if (document != null) {
//            return service.findByCustomerInfo(document)
//                    .map(ResponseEntity::ok)
//                    .orElseGet(() -> ResponseEntity.notFound().build());
//        }
//
//        if (licensePlate != null) {
//            return service.findByVehicleInfo(licensePlate)
//                    .map(order -> ResponseEntity.ok(Collections.singletonList(order)))
//                    .orElseGet(() -> ResponseEntity.notFound().build());
//        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Cancela uma ordem de serviço",
            description = "Altera o status de uma ordem de serviço pelo seu ID para cancelada.")
    @ApiResponse(responseCode = "204", description = "Ordem de serviço cancelada com sucesso")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        serviceOrderController.deleteOrderLogically(id);
    }

    @Operation(summary = "Atualiza uma ordem de serviço existente",
            description = "Atualiza os dados de uma ordem de serviço pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody ServiceOrderRequestDTO request) {
        try {
            return null;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Em diagnóstico.",
            description = "Atualiza a ordem de serviço para: Em diagnóstico.")
    @PatchMapping("/{id}/diagnose")
    public ResponseEntity<ServiceOrderResponseDTO> diagnose(@PathVariable Long id, @RequestParam(required = false) Long employeeId) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Aguardando aprovação.",
            description = "Atualiza a ordem de serviço para: Aguardando aprovação.")
    @PatchMapping("/{id}/wait-for-approval")
    public ResponseEntity<ServiceOrderResponseDTO> waitForApproval(@PathVariable Long id) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Aprovada.",
            description = "Atualiza a ordem de serviço para: Aprovada.")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ServiceOrderResponseDTO> approve(@PathVariable Long id, @RequestParam(required = false) Long employeeId) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Rejeitada.",
            description = "Atualiza a ordem de serviço para: Rejeitada.")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ServiceOrderResponseDTO> reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Em execução ou Aguardando Peças.",
            description = "Atualiza a ordem de serviço para Em execução ou Aguardando Peças a depender da validação do estoque.")
    @PatchMapping("/{id}/execute")
    public ResponseEntity<ServiceOrderResponseDTO> startServiceOrderExecution(@PathVariable Long id) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Finalizada.",
            description = "Atualiza a ordem de serviço para: Finalizada.")
    @PatchMapping("/{id}/finish")
    public ResponseEntity<ServiceOrderResponseDTO> finish(@PathVariable Long id) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Entregue.",
            description = "Atualiza a ordem de serviço para: Entregue.")
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<ServiceOrderResponseDTO> deliver(@PathVariable Long id) {
        try {
            return null;
        } catch (InvalidTransitionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            summary = "Calcula o tempo médio de execução dos serviços",
            description = "Retorna o tempo médio (em horas e formato legível) das ordens de serviço finalizadas, " +
                    "com filtros opcionais por data de início, data de fim e lista de serviços."
    )
    @GetMapping("/average-execution-time")
    public AverageExecutionTimeResponseDTO getAverageExecutionTime(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                                   @RequestParam(required = false) List<Long> serviceIds) {
        return null;
    }

}
