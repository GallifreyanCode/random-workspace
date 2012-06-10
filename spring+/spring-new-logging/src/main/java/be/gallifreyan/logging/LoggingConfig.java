package be.gallifreyan.logging;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/loggingConfig.xml")
@ComponentScan("be.gallifreyan.logging.aspect")
public class LoggingConfig {

}
