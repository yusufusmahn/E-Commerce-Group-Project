package org.jumia.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.jumia.data.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findBySellerId(String sellerId);
    long countBySellerId(String sellerId);
    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findAll();
    Optional<Product> findByIdAndSellerId(String productId, String sellerId);
    List<Product> findBySellerIdAndCategoryId(String sellerId, String categoryId);
    List<Product> findByCategoryId(String categoryId);
    long countByCategoryId(String categoryId);
    List<Product> findByCategoryNameIgnoreCase(String categoryName);






}
