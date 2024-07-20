package com.br.contas.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Contas API")
                        .version("1.0")
                        .description("API de Contas a Pagar")
                        .contact(new Contact().name("Marcus Júnior").email("marcus.junioor@gmail.com")));
    }
}
