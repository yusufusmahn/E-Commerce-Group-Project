package org.jumia.services.customer;

import org.jumia.data.models.Cart;
import org.jumia.data.models.CartItem;
import org.jumia.data.models.Product;
import org.jumia.data.models.User;
import org.jumia.data.respositories.CartItemRepository;
import org.jumia.data.respositories.CartRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.AddToCartRequest;
import org.jumia.dtos.requests.CartItemUpdateRequest;
import org.jumia.dtos.requests.UpdateCartRequest;
import org.jumia.dtos.responses.CartResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerCartServiceImpl implements CustomerCartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Override
    public CartResponse getCartForCurrentUser() {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateCustomer(user);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        return Mapper.mapToCartResponse(cart);
    }

    @Override
    public CartResponse addToCartForCurrentUser(AddToCartRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateCustomer(user);

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(new Cart());
        cart.setUserId(user.getId());

        // ✅ Fix 1: Ensure the cart's item list is never null
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

//        cart.getItems().removeIf(item -> item == null || item.getProduct() == null);


        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProductId()));

        CartItem existingItem = null;

        // ✅ Fix 2: Prevent NullPointerException while looping
        for (CartItem item : cart.getItems()) {
            if (item == null || item.getProduct() == null) continue;

            if (item.getProduct().getId().equals(product.getId())) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            existingItem.setQuantity(newQuantity);
            existingItem.setTotalPrice(product.getPrice() * newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setTotalPrice(product.getPrice() * request.getQuantity());

            cartItemRepository.save(newItem); // manually save since there's no cascade
            cart.getItems().add(newItem);
        }

        cart.setTotalAmount(calculateTotalAmount(cart));
        cart.setLastUpdated(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);
        return Mapper.mapToCartResponse(savedCart);
    }


    @Override
    public CartResponse updateCartForCurrentUser(UpdateCartRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateCustomer(user);

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(new Cart());
        cart.setUserId(user.getId());

        // ✅ Make sure item list is not null
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        // ✅ Delete old items safely
        for (CartItem oldItem : cart.getItems()) {
            if (oldItem != null && oldItem.getId() != null) {
                cartItemRepository.deleteById(oldItem.getId());
            }
        }
        cart.getItems().clear();

        // ✅ Add new items
        List<CartItemUpdateRequest> items = request.getItems();
        for (CartItemUpdateRequest itemRequest : items) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemRequest.getProductId()));

            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setTotalPrice(product.getPrice() * itemRequest.getQuantity());

            cartItemRepository.save(item); // manually save since there's no cascade
            cart.getItems().add(item);
        }

        cart.setTotalAmount(calculateTotalAmount(cart));
        cart.setLastUpdated(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);
        return Mapper.mapToCartResponse(savedCart);
    }


    @Override
    public void removeFromCartForCurrentUser(String productId) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateCustomer(user);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        CartItem itemToRemove = null;
        List<CartItem> updatedItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                itemToRemove = item;
            } else {
                updatedItems.add(item);
            }
        }

        if (itemToRemove == null) {
            throw new ResourceNotFoundException("Product not found in cart.");
        }

        cartItemRepository.deleteById(itemToRemove.getId());
        cart.setItems(updatedItems);
        cart.setTotalAmount(calculateTotalAmount(cart));
        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);
    }

    @Override
    public void clearCartForCurrentUser() {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateCustomer(user);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + user.getId()));

        for (CartItem item : cart.getItems()) {
            cartItemRepository.deleteById(item.getId());
        }

        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);
    }

    private double calculateTotalAmount(Cart cart) {
        double total = 0.0;
        if (cart.getItems() == null) return 0.0;

        for (CartItem item : cart.getItems()) {
            if (item == null || item.getProduct() == null) continue;
            total += item.getTotalPrice();
        }
        return total;
    }




}
