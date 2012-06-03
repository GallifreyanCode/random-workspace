package be.gallifreyan.ws.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

@Configuration
@ImportResource("classpath:spring/wsServerTestConfig.xml")
@ComponentScan({ "be.gallifreyan.ws.server" })
public class WSTestConfig {
	public static final String ORDER_URI = "http://localhost:9773/spring-ws/OrderService";
    public WSTestConfig() {
        super();
    }
    
    @Bean
    public SaajSoapMessageFactory messageFactory() {
    	SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
    	messageFactory.setSoapVersion(SoapVersion.SOAP_11);
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
    	orderServiceTemplate.setDefaultUri(ORDER_URI);
    	return orderServiceTemplate;
    }
}
