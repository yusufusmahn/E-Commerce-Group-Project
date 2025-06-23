package org.temu.services;


import org.temu.dtos.requests.AddToCartRequest;
import org.temu.dtos.responses.CartResponse;

public interface CartService {
    CartResponse addToCart(AddToCartRequest request);
    CartResponse getCartByUserId(String userId);
    void clearCart(String userId);
}
