package org.jumia.controllers.customer;

import org.jumia.dtos.responses.CustomerAnalyticsResponse;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.services.customer.CustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/user")
public class CustomerUserController {

    @Autowired
    private CustomerUserService customerUserService;

    // ✅ Get the customer profile
    @GetMapping("/{customerId}")
    public UserResponse getCustomerProfile(@PathVariable String customerId) {
        return customerUserService.getCustomerProfile(customerId);
    }

    // ✅ Get purchase analytics
    @GetMapping("/{customerId}/analytics")
    public CustomerAnalyticsResponse getPurchaseAnalytics(@PathVariable String customerId) {
        return customerUserService.getPurchaseAnalytics(customerId);
    }
}
