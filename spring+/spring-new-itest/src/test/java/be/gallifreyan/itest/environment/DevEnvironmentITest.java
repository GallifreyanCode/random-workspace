package be.gallifreyan.itest.environment;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceJPAConfig;

public class DevEnvironmentITest extends AbstractEnvironmentITest {
	private static final String DB = "MySQL";
	
	@Test
	public void testDevEnvironment() throws SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles("dev");
		context.scan(PACKAGE);
		context.register(ContextConfig.class, PersistenceJPAConfig.class);
		context.refresh();
		printSpringBeans(context, "dev");
		
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		assertEquals(DB, dataSource.getConnection().getMetaData()
				.getDatabaseProductName());
		context.close();
		context.destroy();
	}
}
