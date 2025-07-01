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
    // Find users by email containing a substring
    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByEmailContainingIgnoreCaseAndRolesContaining(String email, Role role);



}
