package be.gallifreyan.itest.environment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class AbstractEnvironmentITest {
	protected static final String PACKAGE = "be.gallifreyan.config.profile";
	
	protected void printSpringBeans(AnnotationConfigApplicationContext context,
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
