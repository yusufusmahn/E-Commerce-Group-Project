package org.jumia.controllers.seller;

import org.jumia.dtos.requests.UpdateSellerDetailsRequest;
import org.jumia.dtos.requests.UpdateStoreNameRequest;
import org.jumia.dtos.responses.SellerAnalyticsResponse;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.services.seller.SellerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller/user")
public class SellerUserController {

    @Autowired
    private SellerUserService sellerUserService;

    @GetMapping("/getProfile/{sellerId}")
    public UserResponse getSellerProfile(@PathVariable String sellerId) {
        return sellerUserService.getSellerProfile(sellerId);
    }

    @PutMapping("/store-name")
    public UserResponse updateStoreName(@RequestBody UpdateStoreNameRequest request) {
        return sellerUserService.updateStoreDetails(request);
    }

    @GetMapping("/analytics/{sellerId}")
    public SellerAnalyticsResponse getSellerAnalytics(@PathVariable String sellerId) {
        return sellerUserService.getSellerAnalytics(sellerId);
    }

    @PutMapping("/update/{sellerId}/details")
    public UserResponse updateSellerDetails(@PathVariable String sellerId,
                                            @RequestBody UpdateSellerDetailsRequest request) {
        return sellerUserService.updateSellerDetails(sellerId, request);
    }
}
