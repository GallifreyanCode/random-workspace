package be.gallifreyan.itest.logging;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceJPAConfig;

public class LoggerClassITest {

	@Test
	public void testLoggerInjectionInTestEnvironment() throws SQLException, InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingClassTestBean c = (LoggingClassTestBean)context.getBean("loggingClassTestBean");
		Assert.assertNotNull(c.getTestLogger());
	}
	
	@Test
	public void testLoggerInjectionInDevEnvironment() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingClassTestBean c = (LoggingClassTestBean)context.getBean("loggingClassTestBean");
		Assert.assertNotNull(c.getTestLogger());
	}
	
	@Test
	public void testClassLevelLoggingInTestEnvironment() throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingClassTestBean b = (LoggingClassTestBean)context.getBean("loggingClassTestBean");
		b.getLogger();
		b.setTestLogger(LoggerFactory.getLogger(this.getClass()));
	}
	
	@Test
	public void testClassLevelLoggingInDevEnvironment() throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingClassTestBean b = (LoggingClassTestBean)context.getBean("loggingClassTestBean");
		b.getLogger();
		b.setTestLogger(LoggerFactory.getLogger(this.getClass()));
	}
	
	@Test(expected=IOException.class)
	public void testClassLevelLoggingThrowable() throws InterruptedException, IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		LoggingClassTestBean b = (LoggingClassTestBean)context.getBean("loggingClassTestBean");
		b.testThrowable();
	}
}
