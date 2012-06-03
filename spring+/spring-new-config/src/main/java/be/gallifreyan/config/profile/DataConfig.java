package be.gallifreyan.config.profile;

import javax.sql.DataSource;

public interface DataConfig {
	DataSource dataSource();
	String getDatabasePlatform();
}
