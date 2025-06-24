package org.temu.data.respositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.temu.data.models.Role;
import org.temu.data.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRolesContaining(Role role);

}
