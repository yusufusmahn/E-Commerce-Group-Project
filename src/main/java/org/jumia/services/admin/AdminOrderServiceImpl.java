package org.jumia.services.admin;

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

import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public OrderResponse updateOrderAsAdmin(String id, UpdateOrderRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        Order updatedOrder = Mapper.mapUpdateOrderRequestToOrder(request, existingOrder);
        Order savedOrder = orderRepository.save(updatedOrder);
        return Mapper.mapOrderToOrderResponse(savedOrder);
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

    // Optional: remove this if you're validating through RoleValidator instead
    @Override
    public void validateAdminRole(org.springframework.security.core.Authentication authentication) {
        throw new UnsupportedOperationException("Use CurrentUserProvider + RoleValidator instead");
    }
}
