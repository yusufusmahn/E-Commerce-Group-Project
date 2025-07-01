package org.jumia.services.others;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.*;
import org.jumia.data.respositories.*;
import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;
import org.jumia.exceptions.*;
import org.jumia.security.RoleValidator;
import org.jumia.utility.*;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ProductResponse addProduct(CreateProductRequest request, String sellerId) {
        // Validate seller existence and role
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        if (RoleValidator.isNotSeller(seller)) {
            throw new AccessDeniedException("User is not authorized to add products.");
        }

        // Map request to entity and assign seller
        Product product = Mapper.mapCreateProductRequestToProduct(request);
        product.setSellerId(sellerId);

        Product savedProduct = productRepository.save(product);
        return Mapper.mapProductToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(String id, UpdateProductRequest request, String sellerId) {
        // Validate seller existence and role
        User seller = userRepository.findByEmail(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        if (!RoleValidator.isSeller(seller)) {
            throw new AccessDeniedException("User is not authorized to update products.");
        }

        // Fetch and validate product ownership
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (!existingProduct.getSellerId().equals(sellerId)) {
            throw new AccessDeniedException("You can only update your own products.");
        }

        // Update product
        Product updatedProduct = Mapper.mapUpdateProductRequestToProduct(request, existingProduct);
        Product savedProduct = productRepository.save(updatedProduct);
        return Mapper.mapProductToProductResponse(savedProduct);
    }

    @Override
    public void deleteProduct(String id, String sellerId) {
        // Validate seller existence and role
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with ID: " + sellerId));

        if (!RoleValidator.isSeller(seller)) {
            throw new AccessDeniedException("User is not authorized to delete products.");
        }

        // Fetch and validate product ownership
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (!product.getSellerId().equals(sellerId)) {
            throw new AccessDeniedException("You can only delete your own products.");
        }

        productRepository.delete(product);
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return Mapper.mapProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return Mapper.mapProductListToResponseList(products);
    }
}
