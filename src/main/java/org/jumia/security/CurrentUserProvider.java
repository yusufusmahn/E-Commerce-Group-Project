package org.jumia.security;

import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CurrentUserProvider {

    @Autowired
    private UserRepository userRepository;

    public User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Authenticated user not found"));
    }
}
