package be.gallifreyan.ws.client.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ws.client.core.WebServiceTemplate;

import be.gallifreyan.ws.client.config.WSClientTestConfig;

public class ClientRunner {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				WSClientTestConfig.class);
		WebServiceTemplate template = applicationContext
				.getBean("profileServiceTemplate", WebServiceTemplate.class);
		ProfileServiceClient client = new ProfileServiceClient();
		client.webServiceTemplate = template;
		client.invokeProfileServiceAndGetASuccessResponse();
	}
}
