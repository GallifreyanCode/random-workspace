package be.gallifreyan.ws.client.test;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import be.gallifreyan.ws.model.CancelOrderRequest;
import be.gallifreyan.ws.model.CancelOrderResponse;
import be.gallifreyan.ws.model.Order;
import be.gallifreyan.ws.model.PlaceOrderRequest;
import be.gallifreyan.ws.model.PlaceOrderResponse;
import be.gallifreyan.ws.server.service.OrderService;

@Component
public class OrderServiceClient implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceClient.class);

    @Inject
    @Named("orderServiceTemplate")
    private WebServiceTemplate webServiceTemplate;

    public OrderServiceClient() {
    	
    }
    
    public OrderServiceClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }
            
    @Override
    public boolean cancelOrder(String orderRef) {
        logger.debug("Preparing CancelOrderRequest.....");
        CancelOrderRequest request =   new CancelOrderRequest();
        request.setRefNumber(orderRef);

        logger.debug("Invoking Web service Operation[CancelOrder]....");
        CancelOrderResponse response = (CancelOrderResponse) webServiceTemplate.marshalSendAndReceive(request);
            
        logger.debug("Has the order cancelled: " + response.isCancelled());
            
        return response.isCancelled();
    }

    @Override
    public String placeOrder(Order order) {
        logger.debug("Preparing PlaceOrderRequest.....");
                PlaceOrderRequest request = new PlaceOrderRequest();
                request.setOrder(order);
            
        logger.debug("Invoking Web service Operation[PlaceOrder]....");
                PlaceOrderResponse response = (PlaceOrderResponse) webServiceTemplate.marshalSendAndReceive(request);
        logger.debug("Order reference: " + response.getRefNumber());
        return response.getRefNumber();
    }
} 
