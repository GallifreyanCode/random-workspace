package be.gallifreyan.ws.server.endpoint;

import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import be.gallifreyan.ws.model.CancelOrderRequest;
import be.gallifreyan.ws.model.CancelOrderResponse;
import be.gallifreyan.ws.model.ObjectFactory;
import be.gallifreyan.ws.model.PlaceOrderRequest;
import be.gallifreyan.ws.model.PlaceOrderResponse;
import be.gallifreyan.ws.server.service.OrderService;

@Endpoint
public class OrderServicePayloadRootAnnotationEndPoint {
	private OrderService orderService;
	private final ObjectFactory JAXB_OBJECT_FACTORY = new ObjectFactory();

	@Inject
	public OrderServicePayloadRootAnnotationEndPoint(OrderService orderService) {
		this.orderService = orderService;
	}

	@PayloadRoot(localPart = "placeOrderRequest", namespace = "http://www.liverestaurant.com/OrderService/schema")
	public JAXBElement<PlaceOrderResponse> getOrder(
			PlaceOrderRequest placeOrderRequest) {
		PlaceOrderResponse response = JAXB_OBJECT_FACTORY
				.createPlaceOrderResponse();
		response.setRefNumber(orderService.placeOrder(placeOrderRequest
				.getOrder()));

		return new JAXBElement<PlaceOrderResponse>(new QName(
				"http://www.liverestaurant.com/OrderService/schema",
				"placeOrderResponse"), PlaceOrderResponse.class, response);
	}

	@PayloadRoot(localPart = "cancelOrderRequest", namespace = "http://www.liverestaurant.com/OrderService/schema")
	public JAXBElement<CancelOrderResponse> cancelOrder(
			CancelOrderRequest cancelOrderRequest) {
		CancelOrderResponse response = JAXB_OBJECT_FACTORY
				.createCancelOrderResponse();
		response.setCancelled(orderService.cancelOrder(cancelOrderRequest
				.getRefNumber()));
		return new JAXBElement<CancelOrderResponse>(new QName(
				"http://www.liverestaurant.com/OrderService/schema",
				"cancelOrderResponse"), CancelOrderResponse.class, response);
	}

}
