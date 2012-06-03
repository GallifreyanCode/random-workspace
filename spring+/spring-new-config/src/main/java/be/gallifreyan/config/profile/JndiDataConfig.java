package be.gallifreyan.config.profile;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import be.gallifreyan.config.profile.annotation.Production;

@Configuration
@Production
public class JndiDataConfig implements DataConfig {
	@Bean
	public DataSource dataSource() {
		DataSource dataSource = null;
		Context ctx;
		try {
			ctx = new InitialContext();
			dataSource = (DataSource) ctx
					.lookup("java:comp/env/jdbc/datasource");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return dataSource;
	}

	public String getDatabasePlatform() {
		return "";
	}
}
