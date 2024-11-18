package com.migueltarifa.transportesTestAI.configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Crear el contacto
        Contact contact = new Contact()
                .name("Miguel Tarifa")
                .email("migueltarifa@hotmai.com")
                .url("https://www.linkedin.com/in/miguel-tarifa-fern%C3%A1ndez-16443288/");

        // Crear y configurar el objeto Info
        Info apiInfo = new Info()
                .title("API de Transportes")
                .version("1.0")
                .description("Documentación generada para el proyecto de Transportes")
                .contact(contact)
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));

        // Retornar la configuración de OpenAPI
        return new OpenAPI().info(apiInfo);
    }
}
