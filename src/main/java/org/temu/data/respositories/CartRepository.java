package org.temu.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.temu.data.models.Cart;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
