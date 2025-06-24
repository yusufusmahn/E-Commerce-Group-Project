package org.temu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.temu.dtos.requests.*;
import org.temu.dtos.responses.*;
import org.temu.exceptions.TemuException;
import org.temu.services.OrderService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse> placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        try{
            OrderResponse response = orderService.placeOrder(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable("id") String id, @Valid @RequestBody UpdateOrderRequest request) {
        try{
            OrderResponse response = orderService.updateOrder(id, request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable("id") String id) {
        try{
            orderService.cancelOrder(id);
            return new ResponseEntity<>(new ApiResponse("Order canceled successfully", true), HttpStatus.OK);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable("id") String id) {
        try{
            OrderResponse response = orderService.getOrderById(id);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrders() {
        try{
            List<OrderResponse> responses = orderService.getAllOrders();
            return new ResponseEntity<>(new ApiResponse(responses, true), HttpStatus.OK);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }
}
