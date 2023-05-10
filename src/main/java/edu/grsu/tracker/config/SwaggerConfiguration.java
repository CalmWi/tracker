package edu.grsu.tracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@OpenAPIDefinition()
public class SwaggerConfiguration {

    private static final String TOKEN = "Token";

    @Bean
    public OpenAPI openAPI(final SwaggerProperties properties) {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        TOKEN,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .description("Access token")
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER)
                                )
                )
                .security(List.of(new SecurityRequirement().addList(TOKEN)))
                .info(
                        new Info()
                                .title(properties.getTitle())
                                .description(properties.getDescription())
                );
    }

}