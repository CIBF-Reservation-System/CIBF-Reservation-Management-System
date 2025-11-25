package com.cibf.adminservice.admin.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation
 */
@Configuration
public class OpenAPIConfig {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CIBF Admin Service API")
                        .version("1.0.0")
                        .description("Admin Service for Colombo International Book Fair Reservation Management System. " +
                                "Provides comprehensive administrative capabilities including user management, stall oversight, " +
                                "reservation monitoring, system analytics, and audit logging.")
                        .contact(new Contact()
                                .name("CIBF Development Team")
                                .email("admin@cibf.lk"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8085" + contextPath)
                                .description("Local Development Server"),
                        new Server()
                                .url("http://localhost:8080" + contextPath)
                                .description("API Gateway")
                ));
    }
}

