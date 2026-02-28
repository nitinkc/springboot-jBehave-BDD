package com.bookstore.jbehave.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:jbehave}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bookstore User Management API")
                        .version("1.0.0")
                        .description("REST API for user registration and management in the Bookstore application. " +
                                "This API supports user registration, retrieval, update, and deletion operations, " +
                                "along with external user validation via JSONPlaceholder integration.")
                        .contact(new Contact()
                                .name("Bookstore Development Team")
                                .email("dev@bookstore.com")
                                .url("https://github.com/bookstore"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("Test Server")
                ));
    }
}

