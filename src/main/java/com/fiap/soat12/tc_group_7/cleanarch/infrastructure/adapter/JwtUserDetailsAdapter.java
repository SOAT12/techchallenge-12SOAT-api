package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsAdapter implements UserDetailsService {

    private final EmployeeGateway employeeGateway;

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String FALHA_IDENTIFICACAO_MSG = "FALHA NA IDENTIFICAÇÃO: ";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String cpf = username.split(":")[0];

        Employee employee = employeeGateway.findByCpf(cpf)
                .orElseThrow(() -> new UsernameNotFoundException(FALHA_IDENTIFICACAO_MSG + cpf));

        String roleDescription = employee.getEmployeeFunction().getDescription();
        String role = ROLE_PREFIX + roleDescription.toUpperCase().trim();

        List<String> roles = new ArrayList<>();
        roles.add(role);

        return User.builder()
                .username(employee.getCpf())
                .password(employee.getPassword())
                .authorities(roles.toArray(new String[0]))
                .build();
    }
}