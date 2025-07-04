package org.jumia.services.admin;

import org.jumia.dtos.requests.CreateProductRequest;
import org.jumia.dtos.requests.UpdateProductRequest;
import org.jumia.dtos.responses.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminProductService {
//    ProductResponse addProductAsAdmin(CreateProductRequest request);
//    ProductResponse updateProductAsAdmin(String id, UpdateProductRequest request);
    void deleteProductAsAdmin(String id);
    ProductResponse getProductByIdAsAdmin(String id);
    List<ProductResponse> getAllProductsAsAdmin();
    ProductResponse addProductAsAdmin(CreateProductRequest request, MultipartFile image);
    ProductResponse updateProductAsAdmin(String productId, UpdateProductRequest request, MultipartFile image);

}
