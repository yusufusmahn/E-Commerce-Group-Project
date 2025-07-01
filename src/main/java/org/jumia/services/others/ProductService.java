package org.jumia.services.others;


import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(CreateProductRequest request, String sellerId);
    ProductResponse updateProduct(String id, UpdateProductRequest request, String sellerId);
    void deleteProduct(String id, String sellerId);
    ProductResponse getProductById(String id);
    List<ProductResponse> getAllProducts();
}
