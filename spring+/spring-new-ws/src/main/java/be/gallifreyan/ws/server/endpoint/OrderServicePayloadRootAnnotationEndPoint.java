package be.gallifreyan.ws.server.endpoint;

import javax.inject.Inject;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import be.gallifreyan.ws.model.CancelOrderRequest;
import be.gallifreyan.ws.model.CancelOrderResponse;
import be.gallifreyan.ws.model.PlaceOrderRequest;
import be.gallifreyan.ws.model.PlaceOrderResponse;
import be.gallifreyan.ws.server.service.OrderService;

@Endpoint
public class OrderServicePayloadRootAnnotationEndPoint {
	private OrderService orderService;

	@Inject
	public OrderServicePayloadRootAnnotationEndPoint(OrderService orderService) {
		this.orderService = orderService;
	}

	@PayloadRoot(localPart = "placeOrderRequest", namespace = "http://www.liverestaurant.com/OrderService/schema")
	@ResponsePayload
	public PlaceOrderResponse getOrder(@RequestPayload PlaceOrderRequest request) {
		PlaceOrderResponse response = new PlaceOrderResponse();
		response.setRefNumber(orderService.placeOrder(request.getOrder()));
		return response;
	}

	@PayloadRoot(localPart = "cancelOrderRequest", namespace = "http://www.liverestaurant.com/OrderService/schema")
	@ResponsePayload
	public CancelOrderResponse cancelOrder(@RequestPayload CancelOrderRequest request) {
		CancelOrderResponse response = new CancelOrderResponse();
		response.setCancelled(orderService.cancelOrder(request.getRefNumber()));
		return response;
	}
}
