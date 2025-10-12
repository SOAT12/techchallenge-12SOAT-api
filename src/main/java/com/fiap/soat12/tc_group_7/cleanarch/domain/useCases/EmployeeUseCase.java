package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeFunctionGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.EmployeePresenter;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeUseCase {

    private final EmployeeGateway employeeGateway;
    private final EmployeeFunctionGateway employeeFunctionGateway;
    private final EmployeePresenter employeePresenter;

    public List<Employee> getAllEmployees() {
        return employeeGateway.findAll();
    }

    public List<Employee> getAllActiveEmployees() {
        return employeeGateway.findAll().stream()
                .filter(Employee::getActive)
                .toList();
    }

    public Employee getEmployeeById(Long id) {
        return employeeGateway.findById(id)
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));
    }

    public Employee createEmployee(EmployeeRequestDTO requestDTO) {
        EmployeeFunction function = employeeFunctionGateway.findById(requestDTO.getEmployeeFunctionId())
                .orElseThrow(() -> new IllegalArgumentException("Função não encontrada"));
        Employee employee = employeePresenter.toEmployee(requestDTO, function);
        employee.setCreatedAt(new Date());
        employee.setUpdatedAt(new Date());
        employee.setActive(true);
        employee.setUseTemporaryPassword(false);
        return employeeGateway.save(employee);
    }

    public Optional<Employee> updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        return employeeGateway.findById(id).map(existing -> {
            EmployeeFunction function = employeeFunctionGateway.findById(requestDTO.getEmployeeFunctionId())
                    .orElseThrow(() -> new IllegalArgumentException("Função não encontrada"));
            existing.setCpf(requestDTO.getCpf());
            existing.setName(requestDTO.getName());
            existing.setPhone(requestDTO.getPhone());
            existing.setEmail(requestDTO.getEmail());
            existing.setActive(requestDTO.getActive());
            existing.setEmployeeFunction(function);
            existing.setUpdatedAt(new Date());
            return employeeGateway.save(existing);
        });
    }

    public boolean inactivateEmployee(Long id) {
        return employeeGateway.findById(id).map(employee -> {
            employee.setActive(false);
            employee.setUpdatedAt(new Date());
            employee.setUpdatedAt(new Date());

            employeeGateway.save(employee);
            return true;
        }).orElse(false);
    }

    public boolean activateEmployee(Long id) {
        return employeeGateway.findById(id).map(employee -> {
            employee.setActive(true);
            employee.setUpdatedAt(new Date());
            employeeGateway.save(employee);
            return true;
        }).orElse(false);
    }

}
