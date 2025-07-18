package org.jumia.services.admin;

import org.jumia.data.models.OrderStatus;
import org.jumia.data.models.Product;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.UpdateOrderStatusRequest;
import org.jumia.dtos.responses.OrderedProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Order;
import org.jumia.data.models.User;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;



    @Override
    public OrderResponse updateOrderAsAdmin(String id, UpdateOrderRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        List<Product> productList = productRepository.findAll();

        Order updatedOrder = Mapper.mapUpdateOrderRequestToOrder(request, existingOrder, productList);
        Order savedOrder = orderRepository.save(updatedOrder);

        List<OrderedProductResponse> productDetails = Mapper.buildOrderedProductResponses(savedOrder.getProducts());
        return Mapper.mapOrderToOrderResponse(savedOrder, productDetails);
    }


    @Override
    public void cancelOrderAsAdmin(String id) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        List<Order> orders = orderRepository.findAll();
        return Mapper.mapOrderListToResponseList(orders);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(String userId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        List<Order> orders = orderRepository.findByUserId(userId);
        return Mapper.mapOrderListToResponseList(orders);
    }

    @Override
    public void deleteOrder(String id) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        orderRepository.delete(order);
    }


    @Override
    public void updateOrderStatus(String orderId, UpdateOrderStatusRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        // Don't allow updates if order is CANCELLED or RETURNED
        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.RETURNED) {
            throw new IllegalStateException("Cannot update a cancelled or returned order.");
        }

        // Disallow going backwards in status (e.g., DELIVERED -> PROCESSING)
        if (newStatus.ordinal() < currentStatus.ordinal()) {
            throw new IllegalArgumentException("Invalid status transition.");
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }


    @Override
    public void validateAdminRole(org.springframework.security.core.Authentication authentication) {
        throw new UnsupportedOperationException("Use CurrentUserProvider + RoleValidator instead");
    }
}
