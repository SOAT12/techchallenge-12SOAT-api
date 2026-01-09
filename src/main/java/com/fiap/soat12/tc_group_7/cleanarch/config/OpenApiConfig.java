package com.fiap.soat12.tc_group_7.cleanarch.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

//Lembrar de trocar a url la no deployment do kubernets sempre que mudar.
@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "${openapi.service.url:/}",
                        description = "Servidor de Produção (API Gateway)"
                )
        }
)
public class OpenApiConfig { }
