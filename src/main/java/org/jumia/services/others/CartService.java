package org.jumia.services.others;


import org.jumia.dtos.requests.AddToCartRequest;
import org.jumia.dtos.responses.CartResponse;

public interface CartService {
    CartResponse addToCart(AddToCartRequest request);
    CartResponse getCartByUserId(String userId);
    void clearCart(String userId);
//    void clearCartByUserEmail(String userEmail);
    void removeFromCart(String userId, String productId);


}
