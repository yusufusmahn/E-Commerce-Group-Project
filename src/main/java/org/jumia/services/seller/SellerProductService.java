//package org.jumia.services.seller;
//
//import org.jumia.dtos.requests.CreateProductRequest;
//import org.jumia.dtos.requests.UpdateProductRequest;
//import org.jumia.dtos.requests.UpdateStockRequest;
//import org.jumia.dtos.responses.ProductResponse;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//public interface SellerProductService {
//    ProductResponse addProduct(CreateProductRequest request, MultipartFile image, String sellerId);
//    ProductResponse updateProduct(String productId, UpdateProductRequest request, MultipartFile image, String sellerId);
//    void deleteProduct(String productId, String sellerId);
//    List<ProductResponse> getProductsBySellerId(String sellerId);
//    ProductResponse getProductById(String productId, String sellerId);
//    List<ProductResponse> getAllProducts();
//    ProductResponse updateStock(UpdateStockRequest request, String sellerId);
//}
package org.jumia.services.seller;

import org.jumia.dtos.requests.CreateProductRequest;
import org.jumia.dtos.requests.UpdateProductRequest;
import org.jumia.dtos.requests.UpdateStockRequest;
import org.jumia.dtos.responses.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SellerProductService {

    ProductResponse addProduct(CreateProductRequest request, MultipartFile image);

    ProductResponse updateProduct(String productId, UpdateProductRequest request, MultipartFile image);

    void deleteProduct(String productId);

    List<ProductResponse> getProductsBySellerId();

    ProductResponse getProductById(String productId);

    ProductResponse updateStock(UpdateStockRequest request);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductsByCategory(String categoryId);

    List<ProductResponse> getMyProducts(String categoryIdOrName);

}
