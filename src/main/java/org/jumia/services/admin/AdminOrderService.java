package org.jumia.services.admin;

import org.jumia.dtos.requests.UpdateOrderStatusRequest;
import org.springframework.security.core.Authentication;
import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;

import java.util.List;

public interface AdminOrderService {
    OrderResponse updateOrderAsAdmin(String id, UpdateOrderRequest request);
    void cancelOrderAsAdmin(String id);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUserId(String userId);
    void validateAdminRole(Authentication authentication);
    void deleteOrder(String id);
    void updateOrderStatus(String orderId, UpdateOrderStatusRequest request);

}
