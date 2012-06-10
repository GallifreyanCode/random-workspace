package be.gallifreyan.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import be.gallifreyan.logging.LoggingConfig;

@Configuration
@ImportResource("classpath:spring/contextConfig.xml")
@Import({ SecurityConfig.class, LoggingConfig.class})
public class ContextConfig {
	
    public ContextConfig() {
        super();
    }
    
}