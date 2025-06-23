package org.temu.services;


import org.temu.dtos.requests.*;
import org.temu.dtos.responses.*;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(CreateProductRequest request);
    ProductResponse updateProduct(String id, UpdateProductRequest request);
    void deleteProduct(String id);
    ProductResponse getProductById(String id);
    List<ProductResponse> getAllProducts();
}
