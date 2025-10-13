package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter;

import com.fiap.soat12.tc_group_7.cleanarch.interfaces.CodeGeneratorPort;
import com.fiap.soat12.tc_group_7.util.CodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class CodeGeneratorAdapter implements CodeGeneratorPort {

    @Override
    public String generateCode() {
        return CodeGenerator.generateCode();
    }
}