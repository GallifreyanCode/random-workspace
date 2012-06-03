package be.gallifreyan.ws.server.service;

import be.gallifreyan.ws.model.Order;

public interface OrderService {

    String placeOrder(Order order);

    boolean cancelOrder(String orderRef);
}
