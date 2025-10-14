package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ServiceOrderController;
import com.fiap.soat12.tc_group_7.dto.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clean-arch/service-orders")
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
    @Deprecated
    public List<ServiceOrderResponseDTO> getAllOrders() {
        return serviceOrderController.findAllOrders();
    }

    @Operation(summary = "Lista todas as ordens de serviço filtradas por status e prioridades",
            description = "Retorna uma lista de todas as ordens de serviço cadastradas filtradas por status e prioridades.")
    @ApiResponse(responseCode = "200", description = "Lista das ordens de serviço filtradas por status e prioridades retornada com sucesso")
    @GetMapping("/filtered")
    public List<ServiceOrderResponseDTO> getAllOrdersFiltered() {
        return serviceOrderController.findAllOrdersFiltered();
    }

    @Operation(summary = "Lista ordens de serviço por CPF ou Placa",
            description = "Retorna ordens de serviço cadastradas não finalizadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordens de serviço retornada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Informar pelo menos um parâmetro para a busca."),
            @ApiResponse(responseCode = "404", description = "Nenhuma ordem de serviço encontrada para os parâmetros informados.")
    })
    @GetMapping("/consult")
    public List<ServiceOrderResponseDTO> consultOrder(
            @RequestParam(required = false) String document,
            @RequestParam(required = false) String licensePlate) {

        if (document != null) {
            return serviceOrderController.findByCustomerInfo(document);
        }

        if (licensePlate != null) {
            return serviceOrderController.findByVehicleInfo(licensePlate);
        }

        return new ArrayList<>();
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
    public ServiceOrderResponseDTO updateOrder(@PathVariable Long id, @Valid @RequestBody ServiceOrderRequestDTO request) {
        return serviceOrderController.updateServiceOrder(id, request);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Em diagnóstico.",
            description = "Atualiza a ordem de serviço para: Em diagnóstico.")
    @PatchMapping("/{id}/diagnose")
    public ServiceOrderResponseDTO diagnose(@PathVariable Long id, @RequestParam(required = false) Long employeeId) {
        return serviceOrderController.diagnose(id, employeeId);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Aguardando aprovação.",
            description = "Atualiza a ordem de serviço para: Aguardando aprovação.")
    @PatchMapping("/{id}/wait-for-approval")
    public ServiceOrderResponseDTO waitForApproval(@PathVariable Long id) throws MessagingException {
        return serviceOrderController.waitForApproval(id);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Aprovada.",
            description = "Atualiza a ordem de serviço para: Aprovada.")
    @PatchMapping("/{id}/approve")
    public ServiceOrderResponseDTO approve(@PathVariable Long id, @RequestParam(required = false) Long employeeId) {
        return serviceOrderController.approve(id, employeeId);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Rejeitada.",
            description = "Atualiza a ordem de serviço para: Rejeitada.")
    @PatchMapping("/{id}/reject")
    public ServiceOrderResponseDTO reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return serviceOrderController.reject(id, reason);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Em execução ou Aguardando Peças.",
            description = "Atualiza a ordem de serviço para Em execução ou Aguardando Peças a depender da validação do estoque.")
    @PatchMapping("/{id}/execute")
    public ServiceOrderResponseDTO startServiceOrderExecution(@PathVariable Long id) {
        return serviceOrderController.startOrderExecution(id);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Finalizada.",
            description = "Atualiza a ordem de serviço para: Finalizada.")
    @PatchMapping("/{id}/finish")
    public ServiceOrderResponseDTO finish(@PathVariable Long id) throws MessagingException {
        return serviceOrderController.finish(id);
    }

    @Operation(summary = "Atualiza a ordem de serviço para: Entregue.",
            description = "Atualiza a ordem de serviço para: Entregue.")
    @PatchMapping("/{id}/deliver")
    public ServiceOrderResponseDTO deliver(@PathVariable Long id) {
        return serviceOrderController.deliver(id);
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
        return serviceOrderController.calculateAverageExecutionTime(startDate, endDate, serviceIds);
    }

}
