package edu.grsu.tracker.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("springdoc.openapi")
@Getter
@RequiredArgsConstructor
public class SwaggerProperties {

    private final String title;
    private final String description;

}
