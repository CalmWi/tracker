package edu.grsu.tracker.security.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    private String jwtSecret;
    private long jwtExpiration;
}
