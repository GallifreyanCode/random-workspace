package be.gallifreyan.ws.server.service.impl;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import be.gallifreyan.ws.model.Order;
import be.gallifreyan.ws.server.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl() {
    }

    @Override
    public String placeOrder(Order order) {
        logger.info("Order has been placed. Order Info is : " + ObjectUtils.toString(order));
        return getRandomOrderRefNo();
    }

    @Override
    public boolean cancelOrder(String orderRef) {
        logger.info("Order has been placed. Order Reference : " + orderRef);
        return true;
    }
    
    private String getRandomOrderRefNo() {
    	/*
    	 * Calendar calendar = Calendar.getInstance();
         * int year = calendar.get(Calendar.YEAR);
         * int month = calendar.get(Calendar.MONTH);
         * int day = calendar.get(Calendar.DAY_OF_MONTH);
         * return "Ref-" + year + "-" + month + "-" + day + "-" + Math.random();
         */
        return "10";
    }
}
