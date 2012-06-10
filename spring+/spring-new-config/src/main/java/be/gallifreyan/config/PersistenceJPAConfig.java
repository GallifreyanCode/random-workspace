package be.gallifreyan.config;

import java.util.Properties;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.gallifreyan.config.profile.DataConfig;

@Configuration
@EnableTransactionManagement
@ImportResource("classpath:spring/dataConfig.xml")
@ComponentScan(basePackages = "be.gallifreyan", excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebMvcConfig.class))
public class PersistenceJPAConfig {
	private static final boolean showSql = false;

	@Inject
	DataConfig dataConfig;

	public PersistenceJPAConfig() {
		super();
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
		final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataConfig.dataSource());
		factoryBean
				.setPackagesToScan(new String[] { "be.gallifreyan.persistence.entity" });

		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter() {
			{
				setDatabasePlatform("org.hibernate.dialect."
						+ dataConfig.getDatabasePlatform() + "Dialect");
				setShowSql(showSql);
				setGenerateDdl(true);
			}
		};
		factoryBean.setJpaVendorAdapter(vendorAdapter);
		factoryBean.setJpaProperties(additionlProperties());
		return factoryBean;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactoryBean()
				.getObject());
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	final Properties additionlProperties() {
		return new Properties() {
			private static final long serialVersionUID = -5849145376429000522L;
			{
				setProperty("hibernate.hbm2ddl.auto", "create-drop");
			}
		};
	}
}
