package be.gallifreyan.config;

import org.springframework.context.annotation.*;

@Configuration
@ImportResource(value = "classpath:spring/securityConfig.xml")
public class SecurityConfig {
}