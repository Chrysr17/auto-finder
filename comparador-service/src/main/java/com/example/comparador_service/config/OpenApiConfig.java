package com.example.comparador_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - ComparadorService")
                        .version("1.0")
                        .description(
                                "Microservicio para comparar autos con criterios simples y avanzados. "
                                        + "Permite contrastar precio, año, marca y categoría, además de "
                                        + "atributos técnicos como motor, hp, rendimiento y velocidad máxima. "
                                        + "También incorpora contexto de valor con precio de salida estimado "
                                        + "y precio actual aproximado para casos de autos clásicos o de colección."
                        ));

    }

}
