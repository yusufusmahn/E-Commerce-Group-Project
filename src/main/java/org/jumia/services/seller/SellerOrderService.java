package org.jumia.services.seller;

import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;

import java.util.List;

public interface SellerOrderService {
    List<OrderResponse> getOrdersBySellerId();
    OrderResponse updateOrderBySeller(String orderId, UpdateOrderRequest request);
    void cancelOrderBySeller(String orderId);
    List<OrderResponse> getOrdersByStatus(String status);
}
