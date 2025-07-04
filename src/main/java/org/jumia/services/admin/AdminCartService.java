package org.jumia.services.admin;

import org.jumia.dtos.responses.CartResponse;

import java.util.List;

public interface AdminCartService {
    List<CartResponse> getAllCarts(); // View all carts
    void clearCartForUser(String userId); // Clear cart for a specific user
    void removeProductFromAllCarts(String productId); // Remove a product from all carts
}
