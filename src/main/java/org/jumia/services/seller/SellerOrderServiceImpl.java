package org.jumia.services.seller;

import org.jumia.data.models.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Order;
import org.jumia.data.models.User;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.exceptions.AccessDeniedException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

import java.util.List;

@Service
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public List<OrderResponse> getOrdersBySellerId() {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        List<Order> orders = orderRepository.findBySellerId(seller.getId());
        return Mapper.mapOrderListToResponseList(orders);
    }

    @Override
    public OrderResponse updateOrderBySeller(String orderId, UpdateOrderRequest request) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!existingOrder.getSellerId().equals(seller.getId())) {
            throw new AccessDeniedException("You can only update your own orders.");
        }

        Order updatedOrder = Mapper.mapUpdateOrderRequestToOrder(request, existingOrder);
        Order savedOrder = orderRepository.save(updatedOrder);
        return Mapper.mapOrderToOrderResponse(savedOrder);
    }

    @Override
    public void cancelOrderBySeller(String orderId) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getSellerId().equals(seller.getId())) {
            throw new AccessDeniedException("You can only cancel your own orders.");
        }

        orderRepository.delete(order);
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        // ✅ Convert string to enum
        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        List<Order> orders = orderRepository.findBySellerIdAndStatus(seller.getId(), orderStatus);
        return Mapper.mapOrderListToResponseList(orders);
    }

}
