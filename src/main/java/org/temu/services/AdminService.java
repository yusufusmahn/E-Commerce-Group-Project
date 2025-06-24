package org.temu.services;

import org.temu.dtos.requests.AdminProductRequest;
import org.temu.dtos.responses.*;

import java.util.List;

public interface AdminService {
    ProductResponse addProduct(AdminProductRequest request);
    ProductResponse updateProduct(String id, AdminProductRequest request);
    void deleteProduct(String id);
    List<ProductResponse> getAllProducts();
    
    List<OrderResponse> getAllOrders();
    OrderResponse cancelOrder(String id);

    List<UserResponse> getAllUsers();
}
