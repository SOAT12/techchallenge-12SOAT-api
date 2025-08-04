package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import com.fiap.soat12.tc_group_7.service.VehicleServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/vehicle-services")
@RequiredArgsConstructor
@Tag(name = "VehicleService", description = "API para gerenciar serviços")
public class VehicleServiceController {

    private final VehicleServiceService vehicleServiceService;

    @GetMapping
    @Operation(summary = "Busca todos os serviços ativos",
            description = "Retorna uma lista de todos os serviços com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de serviços ativos retornada com sucesso")
    public List<VehicleServiceResponseDTO> getAllActiveVehicleServices() {
        return vehicleServiceService.getAllActiveVehicleServices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um serviço por ID",
            description = "Retorna os dados de um serviço ativo da oficina.")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public VehicleServiceResponseDTO getById(@PathVariable Long id) {
        return vehicleServiceService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Cria um novo serviço",
            description = "Cadastra um novo serviço ativo na oficina.")
    @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso")
    public VehicleServiceResponseDTO create(@RequestBody @Valid VehicleServiceRequestDTO dto) {
        return vehicleServiceService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um serviço",
            description = "Atualiza os dados de um serviço ativo existente.")
    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public VehicleServiceResponseDTO update(@PathVariable Long id,
                                            @RequestBody @Valid VehicleServiceRequestDTO dto) {
        return vehicleServiceService.update(id, dto);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativa um serviço",
            description = "Marca um serviço como inativo.")
    @ApiResponse(responseCode = "204", description = "Serviço desativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public void deactivate(@PathVariable Long id) {
        vehicleServiceService.deactivate(id);
    }

}
