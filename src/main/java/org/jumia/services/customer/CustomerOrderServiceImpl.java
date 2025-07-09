package org.jumia.services.customer;

import org.jumia.data.models.*;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.CreateOrderRequest;
import org.jumia.dtos.requests.OrderedProductDTO;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.dtos.responses.OrderedProductResponse;
import org.jumia.exceptions.AccessDeniedException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public OrderResponse placeOrder(CreateOrderRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        // Fetch full product details based on product IDs
        List<Product> fullProducts = new ArrayList<>();
        for (OrderedProductDTO dto : request.getProducts()) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + dto.getProductId()));
            fullProducts.add(product);
        }

        // Create the Order object
        Order order = Mapper.mapCreateOrderRequestToOrder(request, fullProducts);
        order.setUserId(currentUser.getId());
        order.setStatus(OrderStatus.PENDING); // Always start as PENDING

        // Include the Paystack payment reference
        order.setPaymentReference(request.getPaymentReference());

        // Save the order before redirecting to Paystack or receiving webhook
        Order savedOrder = orderRepository.save(order);

        // Build the product response details
        List<OrderedProductResponse> responseProducts = Mapper.buildOrderedProductResponses(savedOrder.getProducts());

        return Mapper.mapOrderToOrderResponse(savedOrder, responseProducts);
    }


//    @Override
//    public OrderResponse placeOrder(CreateOrderRequest request) {
//        User currentUser = currentUserProvider.getAuthenticatedUser();
//        RoleValidator.validateSelf(currentUser);
//
//        List<Product> fullProducts = new ArrayList<>();
//        for (OrderedProductDTO dto : request.getProducts()) {
//            Product product = productRepository.findById(dto.getProductId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + dto.getProductId()));
//            fullProducts.add(product);
//        }
//
//        Order order = Mapper.mapCreateOrderRequestToOrder(request, fullProducts);
//        order.setUserId(currentUser.getId());
//
//        Order savedOrder = orderRepository.save(order);
//        List<OrderedProductResponse> responseProducts = Mapper.buildOrderedProductResponses(savedOrder.getProducts());
//
//        return Mapper.mapOrderToOrderResponse(savedOrder, responseProducts);
//    }

    @Override
    public List<OrderResponse> getCustomerOrders() {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        List<Order> orders = orderRepository.findByUserId(currentUser.getId());
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found.");
        }

        List<OrderResponse> responses = new ArrayList<>();
        for (Order order : orders) {
            List<OrderedProductResponse> productResponses = Mapper.buildOrderedProductResponses(order.getProducts());
            responses.add(Mapper.mapOrderToOrderResponse(order, productResponses));
        }

        return responses;
    }

    @Override
    public OrderResponse getOrderById(String orderId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only view your own orders.");
        }

        List<OrderedProductResponse> productResponses = Mapper.buildOrderedProductResponses(order.getProducts());
        return Mapper.mapOrderToOrderResponse(order, productResponses);
    }

    @Override
    public void cancelOrder(String orderId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSelf(currentUser);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (!order.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only cancel your own orders.");
        }

        orderRepository.delete(order);
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
            List<OrderedProductResponse> productResponses = Mapper.buildOrderedProductResponses(order.getProducts());
            responses.add(Mapper.mapOrderToOrderResponse(order, productResponses));
        }

        return responses;
    }
}
