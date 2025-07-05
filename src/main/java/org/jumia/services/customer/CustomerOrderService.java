package org.jumia.services.customer;

import org.jumia.dtos.requests.CreateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;

import java.util.List;

public interface CustomerOrderService {
    List<OrderResponse> getCustomerOrders();

    void cancelOrder(String orderId);

    OrderResponse getOrderById(String orderId);

    OrderResponse placeOrder(CreateOrderRequest request);
    List<OrderResponse> getOrdersByStatus(String status);

}
