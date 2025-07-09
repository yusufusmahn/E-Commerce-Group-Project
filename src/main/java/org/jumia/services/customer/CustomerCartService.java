package org.jumia.services.customer;

import org.jumia.dtos.requests.AddToCartRequest;
import org.jumia.dtos.requests.GuestCartItemDTO;
import org.jumia.dtos.requests.UpdateCartRequest;
import org.jumia.dtos.responses.CartResponse;

import java.util.List;

public interface CustomerCartService {
    CartResponse getCartForCurrentUser();
    CartResponse addToCartForCurrentUser(AddToCartRequest request);
    CartResponse updateCartForCurrentUser(UpdateCartRequest request);
    void removeFromCartForCurrentUser(String productId);
    void clearCartForCurrentUser();
    void mergeGuestCart(List<GuestCartItemDTO> items);

}

