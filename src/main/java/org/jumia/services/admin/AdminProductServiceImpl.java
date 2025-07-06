package org.jumia.services.admin;

import org.jumia.data.models.User;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.jumia.services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Product;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.requests.CreateProductRequest;
import org.jumia.dtos.requests.UpdateProductRequest;
import org.jumia.dtos.responses.ProductResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.utility.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AdminProductServiceImpl implements AdminProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public ProductResponse addProductAsAdmin(CreateProductRequest request, MultipartFile image) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(user);

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = cloudinaryService.uploadImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }

        Product product = Mapper.mapCreateProductRequestToProduct(request);
        product.setImageUrl(imageUrl);
        Product savedProduct = productRepository.save(product);
        return Mapper.mapProductToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProductAsAdmin(String productId, UpdateProductRequest request, MultipartFile image) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(user);

        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        String imageUrl = existing.getImageUrl();
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = cloudinaryService.uploadImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }

        Product updated = Mapper.mapUpdateProductRequestToProduct(request, existing);
        updated.setImageUrl(imageUrl);
        Product saved = productRepository.save(updated);
        return Mapper.mapProductToProductResponse(saved);
    }




//    @Override
//    public ProductResponse addProductAsAdmin(CreateProductRequest request) {
//        User user = currentUserProvider.getAuthenticatedUser();
//        RoleValidator.validateAdmin(user); // or validateSuperAdmin(user) if needed
//
//        Product product = Mapper.mapCreateProductRequestToProduct(request);
//        Product savedProduct = productRepository.save(product);
//        return Mapper.mapProductToProductResponse(savedProduct);
//    }
//
//    @Override
//    public ProductResponse updateProductAsAdmin(String id, UpdateProductRequest request) {
//        User user = currentUserProvider.getAuthenticatedUser();
//        RoleValidator.validateAdmin(user);
//
//        Product existingProduct = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
//
//        Product updatedProduct = Mapper.mapUpdateProductRequestToProduct(request, existingProduct);
//        Product savedProduct = productRepository.save(updatedProduct);
//        return Mapper.mapProductToProductResponse(savedProduct);
//    }

    @Override
    public void deleteProductAsAdmin(String id) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(user);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductByIdAsAdmin(String id) {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(user);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return Mapper.mapProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProductsAsAdmin() {
        User user = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateAdmin(user);

        List<Product> products = productRepository.findAll();
        return Mapper.mapProductListToResponseList(products);
    }
}


