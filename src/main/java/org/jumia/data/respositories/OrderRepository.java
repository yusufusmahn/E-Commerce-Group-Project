package org.jumia.data.respositories;

import org.jumia.data.models.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.jumia.data.models.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findBySellerIdAndCreatedAtBetween(String sellerId, LocalDateTime startDate, LocalDateTime endDate);
    long countBySellerId(String userId);
    List<Order> findBySellerIdAndStatus(String sellerId, OrderStatus status);
    List<Order> findBySellerId(String sellerId);
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
    Optional<Order> findByPaymentReference(String reference);



}
