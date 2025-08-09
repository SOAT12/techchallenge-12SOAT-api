package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByDeletedFalse();

    Optional<Customer> findByIdAndDeletedFalse(Long id);

    Optional<Customer> findByCpf(String cpf);

}
