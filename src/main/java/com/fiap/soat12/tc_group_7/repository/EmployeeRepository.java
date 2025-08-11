package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByActiveTrue();
}

