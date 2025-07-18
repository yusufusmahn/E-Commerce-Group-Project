package org.jumia.services.admin;

import org.jumia.data.models.Cart;
import org.jumia.data.models.CartItem;
import org.jumia.data.models.Product;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CartRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.responses.CartResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminCartServiceImpl implements AdminCartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public List<CartResponse> getAllCarts() {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        List<Cart> carts = cartRepository.findAll();
        List<CartResponse> responses = new ArrayList<>();
        for (Cart cart : carts) {
            responses.add(Mapper.mapToCartResponse(cart));
        }
        return responses;
    }

    @Override
    public void clearCartForUser(String userId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
    }


    @Override
    public void removeProductFromAllCarts(String productId) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(currentUser);

        List<Cart> carts = cartRepository.findAll();
        for (Cart cart : carts) {
            boolean modified = cart.getItems().removeIf(item ->
                    item.getProduct() != null && productId.equals(item.getProduct().getId())
            );

            if (modified) {
                cart.setTotalAmount(calculateTotalAmount(cart));
                cartRepository.save(cart);
            }
        }
    }


    private Double calculateTotalAmount(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct() != null && item.getProduct().getPrice() != null) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        return total;
    }

}
