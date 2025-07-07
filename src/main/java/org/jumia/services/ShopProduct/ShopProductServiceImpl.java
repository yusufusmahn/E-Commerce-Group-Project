package org.jumia.services.ShopProduct;

import org.jumia.data.models.Category;
import org.jumia.data.models.Product;
import org.jumia.data.respositories.CategoryRepository;
import org.jumia.data.respositories.ProductRepository;
import org.jumia.dtos.responses.ProductResponse;
import org.jumia.exceptions.EntityNotFoundException;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductServiceImpl implements ShopProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

//    @Override
//    public List<ProductResponse> getProducts(String categoryId) {
//        List<Product> products;
//        if (categoryId != null && !categoryId.isBlank()) {
//            products = productRepository.findByCategoryId(categoryId);
//        } else {
//            products = productRepository.findAll();
//        }
//        return Mapper.mapProductListToResponseList(products);
//    }

    // ShopProductServiceImpl.java
    @Override
    public List<ProductResponse> getProductsByCategoryName(String name) {
        List<Product> products = productRepository.findByCategoryNameIgnoreCase(name);
        return Mapper.mapProductListToResponseList(products);
    }

    @Override
    public List<ProductResponse> getProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return Mapper.mapProductListToResponseList(products);
    }



    @Override
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return Mapper.mapProductToProductResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return Mapper.mapProductListToResponseList(products);
    }

    @Override
    public List<ProductResponse> getAllProducts(String categoryIdOrName) {
        List<Product> products;

        if (categoryIdOrName != null && !categoryIdOrName.isBlank()) {
            // Try ID first
            if (categoryRepository.existsById(categoryIdOrName)) {
                products = productRepository.findByCategoryId(categoryIdOrName);
            }
            // Try name if ID fails
            else {
                Category category = categoryRepository.findByNameIgnoreCase(categoryIdOrName)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryIdOrName));
                products = productRepository.findByCategoryId(category.getId());
            }
        } else {
            products = productRepository.findAll();
        }

        return Mapper.mapProductListToResponseList(products);
    }



}
