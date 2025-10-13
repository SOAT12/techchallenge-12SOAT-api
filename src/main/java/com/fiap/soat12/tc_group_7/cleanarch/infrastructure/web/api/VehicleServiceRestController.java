package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.VehicleServiceController;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicleservice.VehicleServiceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-services")
@RequiredArgsConstructor
@Tag(name = "Serviço", description = "API para gerenciar serviços")
public class VehicleServiceRestController {

    private final VehicleServiceController vehicleServiceController;

    @GetMapping
    @Operation(summary = "Busca todos os serviços ativos",
            description = "Retorna uma lista de todos os serviços com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de serviços ativos retornada com sucesso")
    public List<VehicleServiceResponseDTO> getAllActiveVehicleServices() {
        return vehicleServiceController.getAllActiveVehicleServices();
    }

    @GetMapping("/all")
    @Operation(summary = "Busca todos os serviços ativos",
            description = "Retorna uma lista de todos os serviços com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de serviços ativos retornada com sucesso")
    public List<VehicleServiceResponseDTO> getAllVehicleServices() {
        return vehicleServiceController.getAllVehicleServices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um serviço por ID",
            description = "Retorna os dados de um serviço ativo da oficina.")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public VehicleServiceResponseDTO getById(@PathVariable Long id) {
        return vehicleServiceController.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo serviço",
            description = "Cadastra um novo serviço ativo na oficina.")
    @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso")
    public VehicleServiceResponseDTO create(@RequestBody @Valid VehicleServiceRequestDTO dto) {
        return vehicleServiceController.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um serviço",
            description = "Atualiza os dados de um serviço ativo existente.")
    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public VehicleServiceResponseDTO update(@PathVariable Long id,
                                            @RequestBody @Valid VehicleServiceRequestDTO dto) {
        return vehicleServiceController.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa um serviço",
            description = "Marca um serviço como inativo.")
    @ApiResponse(responseCode = "204", description = "Serviço desativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public void deactivate(@PathVariable Long id) {
        vehicleServiceController.deactivate(id);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Ativa um serviço",
            description = "Marca um serviço como ativo.")
    @ApiResponse(responseCode = "204", description = "Serviço ativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public void activate(@PathVariable Long id) {
        vehicleServiceController.activate(id);
    }

}
