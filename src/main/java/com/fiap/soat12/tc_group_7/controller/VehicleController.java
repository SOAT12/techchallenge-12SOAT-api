package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.VehicleResponseDTO;
import com.fiap.soat12.tc_group_7.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@Tag(name = "Veículo", description = "API para gerenciar veículos")
public class VehicleController {
    
    private final VehicleService vehicleService;


    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Cria um novo veículo",
            description = "Cria um novo veículo com base nos dados fornecidos, associando-o a uma categoria existente.")
    @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@RequestBody @Valid VehicleRequestDTO requestDTO) {
        try {
            VehicleResponseDTO createdVehicle = vehicleService.createVehicle(requestDTO);
            return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém um veículo pelo ID",
            description = "Retorna um veículo específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable Long id){
        return vehicleService.findById(id)
                .map(vehicle -> new ResponseEntity<>(vehicle, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtém um veículo pelo ID",
            description = "Retorna um veículo específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @GetMapping("/plate/{licensePlate}")
    public ResponseEntity<VehicleResponseDTO> getVehicleByLicensePlate(@PathVariable String licensePlate){
        return vehicleService.findByLicensePlate(licensePlate)
                .map(vehicle -> new ResponseEntity<>(vehicle, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lista todos os itens de estoque",
            description = "Retorna uma lista de todos os itens em estoque cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehicles(){
        List<VehicleResponseDTO> vehiclesActive = vehicleService.findAll();
        return new ResponseEntity<>(vehiclesActive, HttpStatus.OK);
    }

    @Operation(summary = "Lista todos os itens de estoque ativos",
            description = "Retorna uma lista de todos os itens em estoque cadastrados e com status ativo")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque ativas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getAllVehiclesActive(){
        List<VehicleResponseDTO> vehiclesActive = vehicleService.findAllVehiclesActive();
        return new ResponseEntity<>(vehiclesActive, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um veículo existente",
            description = "Atualiza os dados de um veículo pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRequestDTO requestDTO) {
        try {
            return vehicleService.updateVehicle(id, requestDTO)
                    .map(updatedVehicle -> new ResponseEntity<>(updatedVehicle, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta um veículo",
            description = "Remove um veículo pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id){
        if (vehicleService.logicallyDeleteVehicle(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reativa um veículo",
            description = "Marca um veículo como ativo pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Veículo reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<VehicleResponseDTO> reactivateVehicle(@PathVariable Long id){
        return vehicleService.reactivateVehicle(id)
                .map(reactivatedVehicle -> new ResponseEntity<>(reactivatedVehicle, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
