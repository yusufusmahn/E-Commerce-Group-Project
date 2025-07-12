package org.jumia.services.seller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.User;
import org.jumia.data.respositories.OrderRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.requests.UpdateSellerDetailsRequest;
import org.jumia.dtos.requests.UpdateStoreNameRequest;
import org.jumia.dtos.responses.SellerAnalyticsResponse;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

@Service
public class SellerUserServiceImpl implements SellerUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public UserResponse getSellerProfile(String sellerId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateOwnershipOrAdmin(currentUser, sellerId);

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));
        return Mapper.toResponse(seller);
    }

    @Override
    public UserResponse updateStoreDetails(UpdateStoreNameRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(currentUser, org.jumia.data.models.Role.SELLER);

        currentUser.setStoreName(Mapper.formatFullName(request.getStoreName()));
        User updatedSeller = userRepository.save(currentUser);
        return Mapper.toResponse(updatedSeller);
    }

    @Override
    public SellerAnalyticsResponse getSellerAnalytics(String sellerId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateOwnershipOrAdmin(currentUser, sellerId);

        long productCount = productRepository.countBySellerId(sellerId);
        long orderCount = orderRepository.countBySellerId(sellerId);

        return new SellerAnalyticsResponse(productCount, orderCount);
    }

    @Override
    public UserResponse updateSellerDetails(String sellerId, UpdateSellerDetailsRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateOwnershipOrAdmin(currentUser, sellerId);

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        seller.setContactInfo(request.getContactInfo());
        seller.setDescription(request.getDescription());

        User updatedSeller = userRepository.save(seller);
        return Mapper.toResponse(updatedSeller);
    }
}
