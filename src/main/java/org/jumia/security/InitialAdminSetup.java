package org.jumia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class InitialAdminSetup {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public ApplicationRunner setupAdminAccounts() {
        return args -> {
            addSuperAdminIfNotExists("superAdmin@system.com", "admin10003");
            addSuperAdminIfNotExists("anotherSuperAdmin@system.com", "admin20003");
        };
    }

    private void addSuperAdminIfNotExists(String email, String password) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User superAdmin = new User();
            superAdmin.setName("Super Admin");
            superAdmin.setEmail(email);
            superAdmin.setPassword(passwordEncoder.encode(password));
            superAdmin.setRoles(new HashSet<>(Set.of(Role.SUPER_ADMIN)));
            superAdmin.setSuperAdmin(true);
            userRepository.save(superAdmin);
            System.out.println("Super Admin user created with email: " + email);
        } else {
            System.out.println("Super Admin already exists with email: " + email);
        }
    }
}

//    @Bean
//    public ApplicationRunner setupAdminAccount() {
//        return args -> {
////            if (userRepository.findByEmail("superAdmin@system.com").isEmpty()) {
//
//                if (userRepository.findByRolesContaining(Role.SUPER_ADMIN).isEmpty()) {
//                User admin = new User();
//                admin.setName("System Admin");
//                admin.setEmail("superAdmin@system.com");
//                admin.setPassword(passwordEncoder.encode("admin10003"));
////                admin.setRoles(Set.of(Role.SUPER_ADMIN));
//                admin.setRoles(new HashSet<>(Set.of(Role.SUPER_ADMIN)));
//                admin.setSuperAdmin(true);
//                userRepository.save(admin);
//                System.out.println("Super Admin user created!");
//            }
//        };
//    }
//}

