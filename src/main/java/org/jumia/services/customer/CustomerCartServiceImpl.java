package org.jumia.services.customer;

import org.jumia.data.models.Cart;
import org.jumia.data.models.Product;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CartRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.AddToCartRequest;
import org.jumia.dtos.requests.UpdateCartRequest;
import org.jumia.dtos.responses.CartResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public CartResponse getCartForCurrentUser() {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAnyRole(user); // Customer or Seller

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));
        return Mapper.mapToCartResponse(cart);
    }

    @Override
    public CartResponse addToCartForCurrentUser(AddToCartRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAnyRole(user);

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(new Cart());
        cart.setUserId(user.getId());

        if (!cart.getProductIds().contains(request.getProductId())) {
            cart.getProductIds().add(request.getProductId());
        }

        cart.setTotalAmount(calculateTotalAmount(cart));
        return Mapper.mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateCartForCurrentUser(UpdateCartRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAnyRole(user);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElse(new Cart());
        cart.setUserId(user.getId());

        cart.setProductIds(request.getProductIds());
        cart.setTotalAmount(calculateTotalAmount(cart));

        return Mapper.mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    public void removeFromCartForCurrentUser(String productId) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAnyRole(user);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        if (!cart.getProductIds().remove(productId)) {
            throw new ResourceNotFoundException("Product not found in cart.");
        }

        cart.setTotalAmount(calculateTotalAmount(cart));
        cartRepository.save(cart);
    }

    @Override
    public void clearCartForCurrentUser() {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAnyRole(user);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        cart.getProductIds().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }

    private double calculateTotalAmount(Cart cart) {
        double total = 0.0;
        for (String productId : cart.getProductIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
            total += product.getPrice();
        }
        return total;
    }
}
