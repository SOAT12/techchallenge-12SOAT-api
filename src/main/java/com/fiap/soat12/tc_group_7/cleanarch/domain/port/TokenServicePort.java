package com.fiap.soat12.tc_group_7.cleanarch.domain.port;

import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;

import java.util.List;

public interface TokenServicePort {

    LoginResponseDTO generateToken(Employee employee, List<String> authorities);
}