package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.service.ServiceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/service-orders")
@Tag(name = "Ordem de Serviço", description = "API para gerenciar ordem de serviço")
public class ServiceOrderController {

    private final ServiceOrderService service;

    public ServiceOrderController(ServiceOrderService orderService) {
        this.service = orderService;
    }


    @Operation(summary = "Cria uma nova ordem de serviço",
            description = "Cria uma nova ordem de serviço com base nos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Ordem de serviço criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public ResponseEntity<ServiceOrderResponseDTO> createOrder(@Valid @RequestBody ServiceOrderRequestDTO request) {
        try {
            return new ResponseEntity<>(service.createServiceOrder(request), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém uma ordem de serviço pelo ID",
            description = "Retorna uma ordem de serviço específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Lista todas as ordens de serviço",
            description = "Retorna uma lista de todas as ordens de serviço cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista das ordens de serviço retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ServiceOrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(service.findAllOrders());
    }

    @Operation(summary = "Cancela uma ordem de serviço",
            description = "Altera o status de uma ordem de serviço pelo seu ID para cancelada.")
    @ApiResponse(responseCode = "204", description = "Ordem de serviço cancelada com sucesso")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (service.deleteOrderLogically(id)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Atualiza uma ordem de serviço existente",
            description = "Atualiza os dados de uma ordem de serviço pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderResponseDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody ServiceOrderRequestDTO request) {
        try {
            return service.updateOrder(id, request)
                    .map(updatedOrder -> new ResponseEntity<>(updatedOrder, HttpStatus.OK))
                    .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Atualiza o status da ordem de serviço",
            description = "Atualiza a ordem de serviço para: Em diagnóstico.")
    @PostMapping("/{id}/diagnose")
    public ResponseEntity<ServiceOrderResponseDTO> diagnose(@PathVariable Long id) {
        return ResponseEntity.ok(service.diagnose(id));
    }

    @Operation(summary = "Atualiza o status da ordem de serviço",
            description = "Atualiza a ordem de serviço para: Aguardando aprovação.")
    @PostMapping("/{id}/wait-for-approval")
    public ResponseEntity<ServiceOrderResponseDTO> waitForApproval(@PathVariable Long id) {
        return ResponseEntity.ok(service.waitForApproval(id));
    }

    @Operation(summary = "Atualiza o status da ordem de serviço",
            description = "Atualiza a ordem de serviço para: Aprovada.")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ServiceOrderResponseDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }

    @Operation(summary = "Atualiza o status da ordem de serviço",
            description = "Atualiza a ordem de serviço para: Rejeitada.")
    @PostMapping("/{id}/reject")
    public ResponseEntity<ServiceOrderResponseDTO> reject(@PathVariable Long id, @RequestParam String reason) {
        return ResponseEntity.ok(service.reject(id, reason));
    }

}
