package org.jumia.services.seller;

import org.jumia.data.models.*;
import org.jumia.dtos.responses.OrderedProductResponse;
import org.jumia.services.audit.GeneralAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.dtos.requests.UpdateOrderRequest;
import org.jumia.dtos.responses.OrderResponse;
import org.jumia.exceptions.AccessDeniedException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private GeneralAuditLogService auditLogService;

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

        List<Product> products = extractProductsFromOrder(existingOrder); // extract embedded product snapshot info
        Order updatedOrder = Mapper.mapUpdateOrderRequestToOrder(request, existingOrder, products);
        Order savedOrder = orderRepository.save(updatedOrder);

        List<OrderedProductResponse> productDetails = Mapper.buildOrderedProductResponses(savedOrder.getProducts());
        return Mapper.mapOrderToOrderResponse(savedOrder, productDetails);
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

        OrderStatus orderStatus;
        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        List<Order> orders = orderRepository.findBySellerIdAndStatus(seller.getId(), orderStatus);
        return Mapper.mapOrderListToResponseList(orders);
    }

    @Override
    public void markOrderAsShipped(String orderId) {
        User seller = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSeller(seller);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getSellerId().equals(seller.getId())) {
            throw new AccessDeniedException("You can only mark your own orders as shipped.");
        }

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Only orders with status PROCESSING can be marked as SHIPPED.");
        }

        order.setStatus(OrderStatus.SHIPPED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        auditLogService.log(
                seller.getId(),
                RoleValidator.getAnyRole(seller), // safely gets any available role
                "MARK_ORDER_SHIPPED",
                "ORDER",
                order.getId(),
                "Seller marked order as SHIPPED"
        );

    }


    private List<Product> extractProductsFromOrder(Order order) {
        List<Product> products = new ArrayList<>();
        for (OrderedProduct op : order.getProducts()) {
            products.add(op.getProduct());
        }
        return products;
    }


}
