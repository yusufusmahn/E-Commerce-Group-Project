package org.jumia.services.customer;

import org.jumia.data.models.Order;
import org.jumia.data.models.User;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.responses.CustomerAnalyticsResponse;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerUserServiceImpl implements CustomerUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public UserResponse getCustomerProfile(String customerId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateOwnershipOrAdmin(currentUser, customerId);

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        return Mapper.toResponse(customer);
    }

    @Override
    public CustomerAnalyticsResponse getPurchaseAnalytics(String customerId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateOwnershipOrAdmin(currentUser, customerId);

        List<Order> orders = orderRepository.findByUserId(customerId);

        double totalSpent = 0.0;
        int totalOrders = 0;

        for (Order order : orders) {
            totalSpent += order.getTotalPrice();
            totalOrders++;
        }

        return new CustomerAnalyticsResponse(totalSpent, totalOrders);
    }
}
