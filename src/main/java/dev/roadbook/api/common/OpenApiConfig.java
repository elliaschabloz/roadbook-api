package dev.roadbook.api.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI roadbookOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Roadbook API")
                .description("Plan and track your trips")
                .version("0.1.0"));
    }
}