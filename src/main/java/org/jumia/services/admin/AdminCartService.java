package org.jumia.services.admin;

import org.jumia.dtos.responses.CartResponse;

import java.util.List;

public interface AdminCartService {
    List<CartResponse> getAllCarts();
    void clearCartForUser(String userId);
    void removeProductFromAllCarts(String productId);
}
