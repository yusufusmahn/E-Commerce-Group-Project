package org.jumia.controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.services.customer.CustomerOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @PostMapping()
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse response = customerOrderService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping()
    public ResponseEntity<List<OrderResponse>> getCustomerOrders() {
        List<OrderResponse> orders = customerOrderService.getCustomerOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse response = customerOrderService.getOrderById(orderId);
        return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        customerOrderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/orders/status")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@RequestParam String status) {
        return ResponseEntity.ok(customerOrderService.getOrdersByStatus(status));
    }

}
