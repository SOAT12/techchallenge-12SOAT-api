package com.fiap.soat12.tc_group_7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Gerenciamento de Oficina")
                .version("1.0")
                .description("API RESTful para gerenciar oficina")
                .contact(new Contact()
                    .name("SOAT12 - Tech Challenge - Group 7"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://springdoc.org")))
            // Adicione a definição de segurança aqui
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            // Aplica a segurança globalmente
            .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"));
    }
}