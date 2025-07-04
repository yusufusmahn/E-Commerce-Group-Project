package org.jumia.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.jumia.data.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findBySellerIdAndCreatedAtBetween(String sellerId, LocalDateTime startDate, LocalDateTime endDate);
    long countBySellerId(String userId);
    List<Order> findBySellerIdAndStatus(String sellerId, String status);
    List<Order> findBySellerId(String sellerId);

}
