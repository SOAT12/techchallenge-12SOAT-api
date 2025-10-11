package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeFunctionGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.EmployeeFunctionPresenter;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeFunctionUseCase {

    private final EmployeeFunctionGateway employeeFunctionGateway;
    private final EmployeeFunctionPresenter employeeFunctionPresenter;

    public List<EmployeeFunction> getAllEmployeeFunctions() {
        return employeeFunctionGateway.findAll();
    }

    public List<EmployeeFunction> getAllActiveEmployeeFunctions() {
        return employeeFunctionGateway.findAll().stream()
                .filter(EmployeeFunction::getActive)
                .toList();
    }

    public Optional<EmployeeFunction> getEmployeeFunctionById(Long id) {
        return employeeFunctionGateway.findById(id);
    }

    public EmployeeFunction createEmployeeFunction(EmployeeFunctionRequestDTO requestDTO) {
        EmployeeFunction entity = employeeFunctionPresenter.toEmployeeFunction(requestDTO);
        entity.setActive(true);
        return employeeFunctionGateway.save(entity);
    }

    public Optional<EmployeeFunction> updateEmployeeFunction(Long id, EmployeeFunctionRequestDTO requestDTO) {
        return employeeFunctionGateway.findById(id).map(existing -> {
            existing.setDescription(requestDTO.getDescription());
//            existing.setActive(requestDTO.getActive()); --- TODO: Ta deixando ativar ao atualizar, pode isso?
            return employeeFunctionGateway.save(existing);
        });
    }

    public boolean inactivateEmployeeFunction(Long id) {
        return employeeFunctionGateway.findById(id).map(employeeFunction -> {
            employeeFunction.setActive(false);
            employeeFunctionGateway.save(employeeFunction);
            return true;
        }).orElse(false);
    }

    public boolean activateEmployeeFunction(Long id) {
        return employeeFunctionGateway.findById(id).map(employeeFunction -> {
            employeeFunction.setActive(true);
            employeeFunctionGateway.save(employeeFunction);
            return true;
        }).orElse(false);
    }

}
