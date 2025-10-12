package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.EmployeeFunctionUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeFunctionGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.EmployeeFunctionPresenter;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EmployeeFunctionController {

    private final EmployeeFunctionRepository employeeFunctionRepository;
    private final EmployeeFunctionPresenter employeeFunctionPresenter;

    public EmployeeFunctionController(EmployeeFunctionRepository employeeFunctionRepository) {
        this.employeeFunctionRepository = employeeFunctionRepository;
        this.employeeFunctionPresenter = new EmployeeFunctionPresenter();
    }

    public EmployeeFunctionResponseDTO createEmployeeFunction(EmployeeFunctionRequestDTO requestDTO) {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        var employeeFunction = employeeFunctionUseCase.createEmployeeFunction(requestDTO);
        return employeeFunctionPresenter.toEmployeeFunctionResponseDTO(employeeFunction);
    }

    public EmployeeFunctionResponseDTO getEmployeeFunctionById(Long id) {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        var employeeFunction = employeeFunctionUseCase.getEmployeeFunctionById(id);
        return employeeFunction
                .map(employeeFunctionPresenter::toEmployeeFunctionResponseDTO)
                .orElseThrow(() -> new NotFoundException("Função não encontrada: " + id));
    }

    public List<EmployeeFunctionResponseDTO> getAllActiveEmployeeFunctions() {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        var employeeFunctions = employeeFunctionUseCase.getAllActiveEmployeeFunctions();
        return employeeFunctions.stream()
                .map(employeeFunctionPresenter::toEmployeeFunctionResponseDTO)
                .toList();
    }

    public List<EmployeeFunctionResponseDTO> getAllEmployeeFunctions() {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        var employeeFunctions = employeeFunctionUseCase.getAllEmployeeFunctions();
        return employeeFunctions.stream()
                .map(employeeFunctionPresenter::toEmployeeFunctionResponseDTO)
                .toList();
    }

    public EmployeeFunctionResponseDTO updateEmployeeFunctionById(Long id, EmployeeFunctionRequestDTO requestDTO) {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        var employeeFunction = employeeFunctionUseCase.updateEmployeeFunction(id, requestDTO);
        return employeeFunction
                .map(employeeFunctionPresenter::toEmployeeFunctionResponseDTO)
                .orElseThrow(() -> new NotFoundException("Erro ao atualizar função: " + id));
    }

    public void inactivateEmployeeFunction(Long id) {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        boolean success = employeeFunctionUseCase.inactivateEmployeeFunction(id);
        if (!success) {
            throw new NotFoundException("Erro ao inativar função: " + id);
        }
    }

    public void activateEmployeeFunction(Long id) {
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeFunctionUseCase employeeFunctionUseCase = new EmployeeFunctionUseCase(employeeFunctionGateway, employeeFunctionPresenter);
        boolean success = employeeFunctionUseCase.activateEmployeeFunction(id);
        if (!success) {
            throw new NotFoundException("Erro ao reativar função: " + id);
        }
    }
}
