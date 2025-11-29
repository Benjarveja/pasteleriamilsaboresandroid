package cl.milsabores.pasteleria.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Mil Sabores API",
                version = "1.0",
                description = "API REST para autenticación, catálogo, checkout y órdenes de Mil Sabores.",
                contact = @Contact(name = "Equipo Mil Sabores", email = "soporte@milsabores.cl"),
                license = @License(name = "Proprietary")
        ),
        servers = {
                @Server(url = "http://localhost:8080/api", description = "Dev local"),
                @Server(url = "https://api.milsabores.cl/api", description = "Prod (referencia)")
        },
        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
