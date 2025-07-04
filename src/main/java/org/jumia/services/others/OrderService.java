package org.jumia.services.others;


import org.jumia.dtos.requests.CreateOrderRequest;
import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest request);
    OrderResponse updateOrder(String id, UpdateOrderRequest request);
    void cancelOrder(String id);
    List<OrderResponse> getOrdersByUserId(String userId);
    List<OrderResponse> getAllOrders();
}
