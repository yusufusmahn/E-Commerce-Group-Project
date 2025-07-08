package org.jumia.controllers.customer;

import org.jumia.dtos.responses.*;
import org.jumia.services.customer.CustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/user")
public class CustomerUserController {

    @Autowired
    private CustomerUserService customerUserService;

    @GetMapping("/{customerId}")
    public UserResponse getCustomerProfile(@PathVariable String customerId) {
        return customerUserService.getCustomerProfile(customerId);
    }

    @GetMapping("/{customerId}/analytics")
    public CustomerAnalyticsResponse getPurchaseAnalytics(@PathVariable String customerId) {
        return customerUserService.getPurchaseAnalytics(customerId);
    }
}
