package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.VehicleController;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Veículo", description = "API para gerenciar veículos")
public class VehicleRestController {

    private final VehicleController vehicleController;

    @Operation(summary = "Cria um novo veículo",
            description = "Cria um novo veículo com base nos dados fornecidos, associando-o a uma categoria existente.")
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public VehicleResponseDTO createVehicle(@RequestBody @Valid VehicleRequestDTO requestDTO) {
        try {
            return vehicleController.createVehicle(requestDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém um veículo pelo ID",
            description = "Retorna um veículo específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @GetMapping("/{id}")
    public VehicleResponseDTO getVehicleById(@PathVariable Long id) {
        return vehicleController.getVehicleById(id);
    }

    @Operation(summary = "Obtém um veículo pela placa informada",
            description = "Retorna um veículo específico pela sua placa.")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @GetMapping("/plate/{licensePlate}")
    public VehicleResponseDTO getVehicleByLicensePlate(@PathVariable String licensePlate) {
        return vehicleController.getVehicleByLicensePlate(licensePlate);
    }

    @Operation(summary = "Lista todos os veículos",
            description = "Retorna uma lista de todos os veículos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque retornada com sucesso")
    @GetMapping("/all")
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleController.getAllVehicles();
    }

    @Operation(summary = "Lista todos os veículos ativos",
            description = "Retorna uma lista de todos os veículos cadastrados e com status ativo")
    @ApiResponse(responseCode = "200", description = "Lista de veículos ativos retornada com sucesso")
    @GetMapping
    public List<VehicleResponseDTO> getAllVehiclesActive() {
        return vehicleController.getAllVehiclesActive();
    }

    @Operation(summary = "Atualiza um veículo existente",
            description = "Atualiza os dados de um veículo pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @PutMapping("/{id}")
    public VehicleResponseDTO updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRequestDTO requestDTO) {
        return vehicleController.updateVehicle(id, requestDTO);
    }

    @Operation(summary = "Deleta um veículo",
            description = "Remove um veículo pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleController.deleteVehicle(id);
    }

    @Operation(summary = "Reativa um veículo",
            description = "Marca um veículo como ativo pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @PatchMapping("/{id}/reactivate")
    public void reactivateVehicle(@PathVariable Long id) {
        vehicleController.reactivateVehicle(id);
    }

}
