package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeFunctionGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeGateway;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EmployeeRepository;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.EmployeeFunctionPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.EmployeePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.EmployeeUseCase;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final EmployeeFunctionRepository employeeFunctionRepository;
    private final EmployeePresenter employeePresenter;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeFunctionRepository employeeFunctionRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeFunctionRepository = employeeFunctionRepository;
        this.employeePresenter = new EmployeePresenter(new EmployeeFunctionPresenter());
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var employee = employeeUseCase.createEmployee(requestDTO);
        return employeePresenter.toEmployeeResponseDTO(employee);
    }

    public EmployeeResponseDTO getEmployeeById(@RequestParam long id) {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var employee = employeeUseCase.getEmployeeById(id);
        return employee
                .map(employeePresenter::toEmployeeResponseDTO)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado: " + id));
    }

    public List<EmployeeResponseDTO> getAllEmployees() {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var employees = employeeUseCase.getAllEmployees();
        return employees.stream()
                .map(employeePresenter::toEmployeeResponseDTO)
                .toList();
    }

    public List<EmployeeResponseDTO> getAllActiveEmployees() {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var employees = employeeUseCase.getAllActiveEmployees();
        return employees.stream()
                .map(employeePresenter::toEmployeeResponseDTO)
                .toList();
    }


    public EmployeeResponseDTO updateEmployeeById(Long id, EmployeeRequestDTO requestDTO) {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var employee = employeeUseCase.updateEmployee(id, requestDTO);
        return employee
                .map(employeePresenter::toEmployeeResponseDTO)
                .orElseThrow(() -> new NotFoundException("Erro ao atualizar funcionário"));
    }

    public void inactivateEmployee(Long id) {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var success = employeeUseCase.inactivateEmployee(id);
        if (!success) {
            throw new NotFoundException("Erro ao inativar funcionário");
        }
    }

    public void activateEmployee(Long id) {
        EmployeeGateway employeeGateway = new EmployeeGateway(employeeRepository);
        EmployeeFunctionGateway employeeFunctionGateway = new EmployeeFunctionGateway(employeeFunctionRepository);
        EmployeeUseCase employeeUseCase = new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter );
        var success = employeeUseCase.activateEmployee(id);
        if (!success) {
            throw new NotFoundException("Erro ao ativar funcionário");
        }
    }
}
