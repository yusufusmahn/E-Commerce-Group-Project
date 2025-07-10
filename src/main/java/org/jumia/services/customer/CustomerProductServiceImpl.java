package org.jumia.services.customer;

import org.jumia.data.models.ProductStatus;
import org.jumia.dtos.requests.ProductSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Product;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.responses.ProductResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.utility.Mapper;

import java.util.List;

@Service
public class CustomerProductServiceImpl implements CustomerProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findByStatus(ProductStatus.APPROVED);
        return Mapper.mapProductListToResponseList(products);

    }

    @Override
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (product.getStatus() != ProductStatus.APPROVED) {
            throw new ResourceNotFoundException("Product is not available");
        }


        return Mapper.mapProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> searchProducts(ProductSearchRequest request) {
        String keyword = request.getKeyword();

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword must not be empty.");
        }

        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndStatus(keyword, ProductStatus.APPROVED);
        return Mapper.mapProductListToResponseList(products);
    }

}
