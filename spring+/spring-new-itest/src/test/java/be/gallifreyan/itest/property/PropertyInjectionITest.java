package be.gallifreyan.itest.property;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceJPAConfig;


public class PropertyInjectionITest {
	
	@Test
	public void testProperyInjectionInTestEnvironment() throws SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		PropertyTestBean p = (PropertyTestBean)context.getBean("propertyTestBean");
		Assert.assertNotNull(p.getTestProperty());
		Assert.assertEquals("succes", p.getTestProperty());
	}
	
	@Test
	public void testProperyInjectionInDevEnvironment() throws SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		
		PropertyTestBean p = (PropertyTestBean)context.getBean("propertyTestBean");
		Assert.assertNotNull(p.getTestProperty());
		Assert.assertEquals("succes", p.getTestProperty());
	}
}
