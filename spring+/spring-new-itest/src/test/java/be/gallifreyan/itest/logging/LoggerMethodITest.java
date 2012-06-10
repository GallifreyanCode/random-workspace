package be.gallifreyan.itest.logging;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceJPAConfig;

public class LoggerMethodITest {

	@Test
	public void testLoggerInjectionInTestEnvironment() throws SQLException, InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingMethodTestBean b = (LoggingMethodTestBean)context.getBean("loggingMethodTestBean");
		Assert.assertNotNull(b.getTestLogger());
	}
	
	@Test
	public void testLoggerInjectionInDevEnvironment() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingMethodTestBean b = (LoggingMethodTestBean)context.getBean("loggingMethodTestBean");
		Assert.assertNotNull(b.getTestLogger());
	}
	
	@Test
	public void testMethodLevelLoggingInTestEnvironment() throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingMethodTestBean b = (LoggingMethodTestBean)context.getBean("loggingMethodTestBean");
		b.getLogger();
	}
	
	@Test
	public void testMethodLevelLoggingInDevEnvironment() throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingMethodTestBean b = (LoggingMethodTestBean)context.getBean("loggingMethodTestBean");
		b.getLogger();
	}
}
