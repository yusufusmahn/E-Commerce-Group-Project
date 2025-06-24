package org.temu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.temu.data.models.Order;
import org.temu.data.respositories.*;
import org.temu.dtos.requests.*;
import org.temu.dtos.responses.*;
import org.temu.exceptions.*;
import org.temu.utility.Mapper;


import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderResponse placeOrder(CreateOrderRequest request) {
        Order order = Mapper.mapCreateOrderRequestToOrder(request);
        Order savedOrder = orderRepository.save(order);
        return Mapper.mapOrderToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse updateOrder(String id, UpdateOrderRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        Order updatedOrder = Mapper.mapUpdateOrderRequestToOrder(request, existingOrder);
        Order savedOrder = orderRepository.save(updatedOrder);
        return Mapper.mapOrderToOrderResponse(savedOrder);
    }

    @Override
    public void cancelOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        orderRepository.delete(order);
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        return Mapper.mapOrderToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            responses.add(Mapper.mapOrderToOrderResponse(order));
        }
        return responses;
    }
}
