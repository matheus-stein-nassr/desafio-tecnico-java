package com.challenge.event_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI eventManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Eventos")
                        .description("Desafio técnico - CRUD de eventos com Spring Boot")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Matheus Stein Nassr")
                                .email("msteinnassr@outlook.com")));
    }
}