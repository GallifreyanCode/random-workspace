package be.gallifreyan.ws.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "be.gallifreyan.ws.server" })
public class WSServerTestConfig {
    public WSServerTestConfig() {
        super();
    }
    
//    @Bean
//    public SaajSoapMessageFactory messageFactory() {
//    	SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
//    	messageFactory.setSoapVersion(SoapVersion.SOAP_12);
//    	return new SaajSoapMessageFactory();
//    }
//    
//    @Bean
//    public Jaxb2Marshaller orderServiceMarshaller() {
//    	Jaxb2Marshaller orderServiceMarshaller = new Jaxb2Marshaller();
//    	orderServiceMarshaller.setContextPath("be.gallifreyan.ws.model");
//    	return orderServiceMarshaller;
//    }
//    
//    @Bean
//    public WebServiceTemplate orderServiceTemplate() {
//    	WebServiceTemplate orderServiceTemplate = new WebServiceTemplate(messageFactory());
//    	Jaxb2Marshaller orderServiceMarshaller = orderServiceMarshaller();
//    	orderServiceTemplate.setMarshaller(orderServiceMarshaller);
//    	orderServiceTemplate.setUnmarshaller(orderServiceMarshaller);
//    	orderServiceTemplate.setMessageSender(new CommonsHttpMessageSender());
//    	orderServiceTemplate.setDefaultUri(ORDER_URI);
//    	return orderServiceTemplate;
//    }
}
