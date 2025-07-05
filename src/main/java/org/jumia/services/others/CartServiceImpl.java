package org.jumia.services.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Cart;
import org.jumia.data.models.Product;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CartRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.requests.AddToCartRequest;
import org.jumia.dtos.responses.CartResponse;
import org.jumia.exceptions.AccessDeniedException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CartResponse addToCart(AddToCartRequest request) {
        // Find existing cart or create a new one
        Cart cart = cartRepository.findByUserId(request.getUserId()).orElse(new Cart());
        cart.setUserId(request.getUserId());

        // Avoid duplicate product IDs
        if (!cart.getProductIds().contains(request.getProductId())) {
            cart.getProductIds().add(request.getProductId());
        }

        // Recalculate total amount
        cart.setTotalAmount(calculateTotalAmount(cart));

        // Save and return response
        Cart updatedCart = cartRepository.save(cart);
        return Mapper.mapToCartResponse(updatedCart);
    }

    @Override
    public CartResponse getCartByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (RoleValidator.isNotCustomer(user)) {
            throw new AccessDeniedException("Only customers can access carts.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));
        return Mapper.mapToCartResponse(cart);
    }

    @Override
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));
        cart.getProductIds().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }

    @Override
    public void removeFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        if (cart.getProductIds().contains(productId)) {
            cart.getProductIds().remove(productId);
            cart.setTotalAmount(calculateTotalAmount(cart));
            cartRepository.save(cart);
        } else {
            throw new ResourceNotFoundException("Product not found in cart: " + productId);
        }
    }

    // Helper method to calculate the total amount of the cart
    private Double calculateTotalAmount(Cart cart) {
        double total = 0.0;
        for (String productId : cart.getProductIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
            total += product.getPrice();
        }
        return total;
    }
}
