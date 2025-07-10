package org.jumia.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRolesContaining(Role role);
    Optional<User> findById(String id);
    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByEmailContainingIgnoreCaseAndRolesContaining(String email, Role role);
    Optional<User> findByResetToken(String token);
    boolean existsByEmailIgnoreCase(String email);




}
