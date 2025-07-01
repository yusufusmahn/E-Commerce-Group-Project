package org.jumia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;

import java.util.Set;

@Component
public class InitialAdminSetup {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner setupAdminAccount() {
        return args -> {
            if (userRepository.findByRolesContaining(Role.ADMIN).isEmpty()) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin1003"));
                admin.setRoles(Set.of(Role.SUPER_ADMIN));
                admin.setSuperAdmin(true);
                userRepository.save(admin);
                System.out.println("Super Admin user created!");
            }
        };
    }
}
