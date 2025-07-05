package org.jumia.services.customer;

import org.jumia.data.models.*;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.responses.OrderedProductResponse;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.dtos.requests.CreateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.exceptions.AccessDeniedException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.utility.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<OrderResponse> getCustomerOrders() {
        User currentUser = currentUserProvider.getAuthenticatedUser();

        // Ensure user is authenticated
        RoleValidator.validateSelf(currentUser);

        List<Order> orders = orderRepository.findByUserId(currentUser.getId());
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for your account.");
        }

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderedProductResponse> products = buildOrderedProductResponses(order.getProducts());
            responses.add(Mapper.mapOrderToOrderResponse(order, products));
        }

        return responses;
    }



    @Override
    public void cancelOrder(String orderId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only cancel your own orders.");
        }

        orderRepository.delete(order);
    }



    @Override
    public OrderResponse getOrderById(String orderId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only view your own orders.");
        }

        List<OrderedProductResponse> productDetails = buildOrderedProductResponses(order.getProducts());
        return Mapper.mapOrderToOrderResponse(order, productDetails);
    }




    @Override
    public OrderResponse placeOrder(CreateOrderRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();

        // validate that a user is authenticated
        RoleValidator.validateSelf(currentUser);

        Order order = Mapper.mapCreateOrderRequestToOrder(request);
        order.setUserId(currentUser.getId());

        Order savedOrder = orderRepository.save(order);

        List<OrderedProductResponse> productDetails = buildOrderedProductResponses(savedOrder.getProducts());

        return Mapper.mapOrderToOrderResponse(savedOrder, productDetails);
    }




    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        List<Order> orders = orderRepository.findByUserIdAndStatus(currentUser.getId(), orderStatus);

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderedProductResponse> products = buildOrderedProductResponses(order.getProducts());
            responses.add(Mapper.mapOrderToOrderResponse(order, products));
        }

        return responses;
    }



    private List<OrderedProductResponse> buildOrderedProductResponses(List<OrderedProduct> orderedProducts) {
        List<OrderedProductResponse> responseList = new ArrayList<>();

        for (OrderedProduct op : orderedProducts) {
            Product product = productRepository.findById(op.getProduct().getId()).orElse(null);

            OrderedProductResponse dto = new OrderedProductResponse();
            dto.setProductId(op.getProduct().getId());
            dto.setQuantity(op.getQuantity());
            dto.setName(product != null ? product.getName() : "Unknown Product");

            responseList.add(dto);
        }

        return responseList;
    }



}


/*
@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderResponse> getCustomerOrders(String customerId) {
        List<Order> orders = orderRepository.findByUserId(customerId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for customer ID: " + customerId);
        }
        return Mapper.mapOrderListToResponseList(orders);
    }

    @Override
    public void cancelOrder(String customerId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(customerId)) {
            throw new AccessDeniedException("You can only cancel your own orders.");
        }

        orderRepository.delete(order);
    }

    @Override
    public OrderResponse getOrderById(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(customerId)) {
            throw new AccessDeniedException("You can only view your own orders.");
        }

        return Mapper.mapOrderToOrderResponse(order);
    }

    @Override
    public OrderResponse placeOrder(CreateOrderRequest request, String customerId) {
        Order order = Mapper.mapCreateOrderRequestToOrder(request);
        order.setUserId(customerId);
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        return Mapper.mapOrderToOrderResponse(savedOrder);
    }

 */

