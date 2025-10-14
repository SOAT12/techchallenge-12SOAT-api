package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.port.CodeGeneratorPort;
import com.fiap.soat12.tc_group_7.cleanarch.util.CodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class CodeGeneratorAdapter implements CodeGeneratorPort {

    @Override
    public String generateCode() {
        return CodeGenerator.generateCode();
    }
}