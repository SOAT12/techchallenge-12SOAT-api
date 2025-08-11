package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByActiveTrue();

    List<Employee> findAllByEmployeeFunction_descriptionAndActiveTrue(String employeeFunctionDescription);

    Optional<Employee> findByCpf(String cpf);
}

