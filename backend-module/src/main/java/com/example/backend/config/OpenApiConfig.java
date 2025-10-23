package com.example.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Benefícios API",
                version = "v1",
                description = "API do backend para gestão de benefícios e transferência entre contas.",
                contact = @Contact(name = "Equipe BIP", email = "contato@example.com"),
                license = @License(name = "MIT")
        )
)
public class OpenApiConfig {
    // Configuração básica via anotação. Sem beans necessários para o springdoc starter.
}
