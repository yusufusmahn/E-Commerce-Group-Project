package org.temu.services;


import org.temu.dtos.requests.CreateOrderRequest;
import org.temu.dtos.requests.UpdateOrderRequest;
import org.temu.dtos.responses.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest request);
    OrderResponse updateOrder(String id, UpdateOrderRequest request);
    void cancelOrder(String id);
    OrderResponse getOrderById(String id);
    List<OrderResponse> getAllOrders();
}
