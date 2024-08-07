package ru.zhadaev.taskmanagementsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Management System",
                description = "Test task from the Effective Mobile company", version = "1.0.0",
                contact = @Contact(
                        name = "Zhadaev Alexander",
                        email = "zhadaevs@yandex.ru",
                        url = "https://github.com/ZhadaevAlex"
                )
        )
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
