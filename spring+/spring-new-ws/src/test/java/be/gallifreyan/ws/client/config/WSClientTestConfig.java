package be.gallifreyan.ws.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

@Configuration
@ComponentScan({ "be.gallifreyan.ws.client.test" })
public class WSClientTestConfig {
	public static final String WS_URI = "http://localhost:9773/ws";
	public static final String ORDER_URI = "http://localhost:9773/ws/OrderService";
	public static final String PROFILE_URI = "http://localhost:9773/ws/ProfileService";
	
	public WSClientTestConfig() {
		super();
	}

	@Bean
	public SaajSoapMessageFactory messageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return new SaajSoapMessageFactory();
	}

	@Bean
	public Jaxb2Marshaller orderServiceMarshaller() {
		Jaxb2Marshaller orderServiceMarshaller = new Jaxb2Marshaller();
		orderServiceMarshaller.setContextPath("be.gallifreyan.ws.model");
		return orderServiceMarshaller;
	}

	@Bean
	public WebServiceTemplate orderServiceTemplate() {
		WebServiceTemplate orderServiceTemplate = new WebServiceTemplate(messageFactory());
		Jaxb2Marshaller orderServiceMarshaller = orderServiceMarshaller();
		orderServiceTemplate.setMarshaller(orderServiceMarshaller);
		orderServiceTemplate.setUnmarshaller(orderServiceMarshaller);
		orderServiceTemplate.setMessageSender(new CommonsHttpMessageSender());
		orderServiceTemplate.setDefaultUri(WS_URI);
		return orderServiceTemplate;
	}
	
	@Bean
	public WebServiceTemplate profileServiceTemplate() {
		WebServiceTemplate orderServiceTemplate = new WebServiceTemplate(messageFactory());
		Jaxb2Marshaller orderServiceMarshaller = orderServiceMarshaller();
		orderServiceTemplate.setMarshaller(orderServiceMarshaller);
		orderServiceTemplate.setUnmarshaller(orderServiceMarshaller);
		//orderServiceTemplate.setMessageSender(new CommonsHttpMessageSender());
		orderServiceTemplate.setDefaultUri(WS_URI);
		return orderServiceTemplate;
	}
}
