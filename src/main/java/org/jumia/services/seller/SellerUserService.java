// SellerUserService.java (Interface)
package org.jumia.services.seller;

import org.jumia.dtos.requests.UpdateSellerDetailsRequest;
import org.jumia.dtos.requests.UpdateStoreNameRequest;
import org.jumia.dtos.responses.SellerAnalyticsResponse;
import org.jumia.dtos.responses.UserResponse;

public interface SellerUserService {
    UserResponse getSellerProfile(String sellerId);
    UserResponse updateStoreDetails(UpdateStoreNameRequest request);
    SellerAnalyticsResponse getSellerAnalytics(String sellerId);
    UserResponse updateSellerDetails(String sellerId, UpdateSellerDetailsRequest request);
}
