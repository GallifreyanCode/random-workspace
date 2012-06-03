package be.gallifreyan.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.adapter.GenericMarshallingMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

import be.gallifreyan.ws.server.endpoint.OrderServicePayloadRootAnnotationEndPoint;
import be.gallifreyan.ws.server.service.impl.OrderServiceImpl;

@Configuration
public class WSConfig {
	@Bean
	public PayloadRootAnnotationMethodEndpointMapping interceptors() {
		PayloadRootAnnotationMethodEndpointMapping interceptors = new PayloadRootAnnotationMethodEndpointMapping();
		EndpointInterceptor[] endpointInterceptors = {new PayloadLoggingInterceptor()};
		interceptors.setInterceptors(endpointInterceptors);
		return interceptors;
	}
	
	@Bean
	public OrderServicePayloadRootAnnotationEndPoint orderServiceEndpoint() {
		return new OrderServicePayloadRootAnnotationEndPoint(new OrderServiceImpl());
	}
	
	@Bean
	public DefaultWsdl11Definition orderService() {
		DefaultWsdl11Definition orderService = new DefaultWsdl11Definition();
		orderService.setSchema(orderServiceSchema());
		orderService.setPortTypeName("OrderService");
		orderService.setLocationUri("http://www.liverestaurant.com/OrderService/");
		orderService.setTargetNamespace("http://www.liverestaurant.com/OrderService/");
		return orderService;
	}

	@Bean
	public SimpleXsdSchema orderServiceSchema() {
		SimpleXsdSchema orderServiceSchema = new SimpleXsdSchema();
		ClassPathResource xsd = new ClassPathResource("xsd/OrderService.xsd", WSConfig.class);
		orderServiceSchema.setXsd(xsd);
		return orderServiceSchema;
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public GenericMarshallingMethodEndpointAdapter genericMarshallingMethodEndpointAdapter() {
		return new GenericMarshallingMethodEndpointAdapter(marshaller());
	}
	
//	New Way, doesn't work yet
//	@Bean
//	public MarshallingPayloadMethodProcessor marshallingPayloadMethodProcessor() {
//		return new MarshallingPayloadMethodProcessor(marshaller(), marshaller());
//	}
//
//	@Bean
//	public DefaultMethodEndpointAdapter defaultMethodEndpointAdapter() {
//		DefaultMethodEndpointAdapter defaultMethodEndpointAdapter = new DefaultMethodEndpointAdapter();
//		
//		List<MethodArgumentResolver> methodArgumentResolvers = new ArrayList<MethodArgumentResolver>();
//		methodArgumentResolvers.add(marshallingPayloadMethodProcessor());
//		defaultMethodEndpointAdapter.setMethodArgumentResolvers(methodArgumentResolvers);
//		
//		List<MethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<MethodReturnValueHandler>();
//		methodReturnValueHandlers.add(marshallingPayloadMethodProcessor());
//		defaultMethodEndpointAdapter.setMethodReturnValueHandlers(methodReturnValueHandlers);
//		return defaultMethodEndpointAdapter;
//	}
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller orderServiceMarshaller = new Jaxb2Marshaller();
		orderServiceMarshaller.setContextPath("be.gallifreyan.ws.model");
		return orderServiceMarshaller;
	}
}
