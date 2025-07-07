package org.jumia.services.ShopProduct;

import org.jumia.dtos.responses.ProductResponse;
import java.util.List;

public interface ShopProductService {
//    List<ProductResponse> getProducts(String categoryId);
    ProductResponse getProductById(String productId);
    List<ProductResponse> getProductsByCategory(String categoryId);
    List<ProductResponse> getAllProducts(String categoryIdOrName);
    List<ProductResponse> getProductsByCategoryName(String name);
    List<ProductResponse> getProductsByName(String name);


}
