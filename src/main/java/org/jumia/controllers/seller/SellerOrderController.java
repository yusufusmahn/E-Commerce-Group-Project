package org.jumia.controllers.seller;

import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.services.seller.SellerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    @Autowired
    private SellerOrderService sellerOrderService;

    @GetMapping
    public List<OrderResponse> getOrdersBySeller() {
        return sellerOrderService.getOrdersBySellerId();
    }

    @GetMapping("/status/{status}")
    public List<OrderResponse> getOrdersByStatus(@PathVariable String status) {
        return sellerOrderService.getOrdersByStatus(status);
    }

    @PutMapping("/{orderId}")
    public OrderResponse updateOrder(
            @PathVariable String orderId,
            @RequestBody UpdateOrderRequest request) {
        return sellerOrderService.updateOrderBySeller(orderId, request);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable String orderId) {
        sellerOrderService.cancelOrderBySeller(orderId);
    }
}
