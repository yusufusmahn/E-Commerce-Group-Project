package org.jumia.controllers.Admin;

import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.services.admin.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private AdminOrderService adminOrderService;

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable String id, @Valid @RequestBody UpdateOrderRequest request) {
        OrderResponse updated = adminOrderService.updateOrderAsAdmin(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        adminOrderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = adminOrderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderResponse> userOrders = adminOrderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(userOrders);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        adminOrderService.cancelOrderAsAdmin(id);
        return ResponseEntity.ok("Order canceled successfully.");
    }
}
