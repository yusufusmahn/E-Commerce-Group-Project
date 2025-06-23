package org.temu.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.temu.data.models.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
}
