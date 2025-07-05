package org.jumia.services.customer;

import org.jumia.dtos.responses.CustomerAnalyticsResponse;
import org.jumia.dtos.responses.UserResponse;

public interface CustomerUserService {
    UserResponse getCustomerProfile(String customerId);
    CustomerAnalyticsResponse getPurchaseAnalytics(String customerId);
}
