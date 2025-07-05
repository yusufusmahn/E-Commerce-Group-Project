package org.jumia.services.customer;

import org.jumia.dtos.requests.ProductSearchRequest;
import org.jumia.dtos.responses.ProductResponse;

import java.util.List;

public interface CustomerProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(String productId);
    List<ProductResponse> searchProducts(ProductSearchRequest request);
}
