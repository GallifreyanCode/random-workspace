package be.gallifreyan.config;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestEnvironments {

	@Test
	public void testDevEnvironment() throws SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		printSpringBeans(context, "dev");

		DataSource dataSource = (DataSource) context.getBean("dataSource");
		assertEquals("MySQL", dataSource.getConnection().getMetaData()
				.getDatabaseProductName());
		context.close();
		context.destroy();
	}

	@Test
	public void testTestEnvironment() throws SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("test");
		context.scan("be.gallifreyan.config.profile");
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		printSpringBeans(context, "test");

		DataSource dataSource = (DataSource) context.getBean("dataSource");
		assertEquals("HSQL Database Engine", dataSource.getConnection()
				.getMetaData().getDatabaseProductName());
	}

	private void printSpringBeans(AnnotationConfigApplicationContext context,
			String profile) {
		System.out.println("****************************Spring Beans "
				+ profile);
		for (String bean : context.getBeanDefinitionNames()) {
			if (!bean.contains("org.springframework.")) {
				System.out.println(bean);
			}
		}
	}
}
