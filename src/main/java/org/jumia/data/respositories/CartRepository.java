package org.jumia.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.jumia.data.models.Cart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
    List<Cart> findByLastUpdatedBefore(LocalDateTime cutoff);

}
