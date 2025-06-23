package org.temu.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.temu.data.models.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
