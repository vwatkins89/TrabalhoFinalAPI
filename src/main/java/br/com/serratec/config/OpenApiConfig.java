package br.com.serratec.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceOpenAPI() {
        Server devServer = new Server()
            .url("http://localhost:8080")
            .description("Servidor de desenvolvimento");

        Server prodServer = new Server()
            .url("https://api.seudominio.com")
            .description("Servidor de produção");

        Contact contact = new Contact()
            .name("Victor")
            .email("victor@gmail.com")
            .url("https://www.meudominio.com.br");

        Info info = new Info()
            .title("ecommerce-api")
            .version("1.0")
            .termsOfService("API para gerenciamento de produtos, usuários e pedidos")
            .contact(contact);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer, prodServer));
    }
}