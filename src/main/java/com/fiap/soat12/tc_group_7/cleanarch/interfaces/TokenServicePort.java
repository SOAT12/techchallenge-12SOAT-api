package com.fiap.soat12.tc_group_7.cleanarch.interfaces;


import com.fiap.soat12.tc_group_7.cleanarch.entity.Employee;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;

import java.util.List;

public interface TokenServicePort {

    LoginResponseDTO generateToken(Employee employee, List<String> authorities);
}