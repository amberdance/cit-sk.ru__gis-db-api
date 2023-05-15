package ru.hard2code.gisdbapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"prod", "dev"})
@OpenAPIDefinition(servers = {@Server(url = "/", description = "${app.name}")})

public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi(@Value("${app.name}") String appName) {
        return GroupedOpenApi.builder()
                .group(appName)
                .displayName(appName)
                .packagesToScan("ru.hard2code.gisdbapi.controller")
                .build();
    }
}
