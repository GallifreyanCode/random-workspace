package be.gallifreyan.ws.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.ws.server.EndpointAdapter;
import org.springframework.ws.server.MessageDispatcher;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.WebServiceMessageReceiverHandlerAdapter;
import org.springframework.ws.transport.http.WsdlDefinitionHandlerAdapter;

/**
 * WSDL: http://localhost:8080/{projectName}/{root-path}/ws/{bean-name}.wsdl
 * Example: http://localhost:8080/spring-ws/krams/ws/subscription.wsdl Endpoint:
 * Service: http://localhost:8080/{projectName}/{root-path}/ws Example:
 * http://localhost:8080/spring-ws/krams/ws
 */
@Configuration
@ImportResource("classpath:spring/spring-ws.xml")
@ComponentScan("be.gallifreyan.ws")
public class WSConfig {

	@Bean
	public SaajSoapMessageFactory messageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return new SaajSoapMessageFactory();
	}

	@Bean
	public WebServiceMessageReceiverHandlerAdapter messageHandlerAdapter() {
		WebServiceMessageReceiverHandlerAdapter messageHandlerAdapter = new WebServiceMessageReceiverHandlerAdapter();
		messageHandlerAdapter.setMessageFactory(messageFactory());
		return messageHandlerAdapter;
	}

	@Bean
	public WsdlDefinitionHandlerAdapter wdslHandlerAdapter() {
		return new WsdlDefinitionHandlerAdapter();
	}

	@Bean
	public MessageDispatcher messageDispatcher() {
		MessageDispatcher messageDispatcher = new MessageDispatcher();
		List<EndpointAdapter> endpointAdapters = new ArrayList<EndpointAdapter>();
		endpointAdapters.add(defaultMethodEndpointAdapter());
		messageDispatcher.setEndpointAdapters(endpointAdapters);
		return messageDispatcher;
	}

	@Bean
	public DefaultMethodEndpointAdapter defaultMethodEndpointAdapter() {
		DefaultMethodEndpointAdapter defaultMethodEndpointAdapter = new DefaultMethodEndpointAdapter();

		List<MethodArgumentResolver> methodArgumentResolvers = new ArrayList<MethodArgumentResolver>();
		methodArgumentResolvers.add(marshallingPayloadMethodProcessor());
		defaultMethodEndpointAdapter
				.setMethodArgumentResolvers(methodArgumentResolvers);

		List<MethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<MethodReturnValueHandler>();
		methodReturnValueHandlers.add(marshallingPayloadMethodProcessor());
		defaultMethodEndpointAdapter
				.setMethodReturnValueHandlers(methodReturnValueHandlers);
		return defaultMethodEndpointAdapter;
	}
	
	@Bean
	public MarshallingPayloadMethodProcessor marshallingPayloadMethodProcessor() {
		return new MarshallingPayloadMethodProcessor(marshaller(), marshaller());
	}

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller orderServiceMarshaller = new Jaxb2Marshaller();
		orderServiceMarshaller.setContextPath("be.gallifreyan.ws.model");
		return orderServiceMarshaller;
	}
	
	@Bean
	public SimpleUrlHandlerMapping simpleUrlHandler() {
		SimpleUrlHandlerMapping simpleUrlHandler = new SimpleUrlHandlerMapping();
		Properties mappings = new Properties();
		mappings.put("/ws", "messageDispatcher");
		mappings.put("/ws/orderservice.wsdl", "orderservice");
		mappings.put("/ws/userservice.wsdl", "userservice");
		simpleUrlHandler.setMappings(mappings);
		return simpleUrlHandler;
	}
	
	@Bean
	public SimpleControllerHandlerAdapter controllerHandlerAdapter() {
		return new SimpleControllerHandlerAdapter();
	}
}
