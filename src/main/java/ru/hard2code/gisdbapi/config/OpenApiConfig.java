package ru.hard2code.gisdbapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod", "dev"})
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Amberdance",
                        email = "amberdance@yandex.run",
                        url = "https://hard2code.ru"
                ),
                description = "${app.name}",
                title = "OpenApi specification"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:${server.port:8080}"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://api.gis.hard2code.ru"
                )
        }
)
public class OpenApiConfig {

}
