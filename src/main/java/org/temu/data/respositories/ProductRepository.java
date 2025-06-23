package org.temu.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.temu.data.models.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
