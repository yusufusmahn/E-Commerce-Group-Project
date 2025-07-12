package seed;

import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SuperAdminSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void seed() {
        String email = "superadmin@jumia.com";

        if (userRepository.existsByEmailIgnoreCase(email)) {
            System.out.println("Super admin already exists. Skipping...");
            return;
        }

        User superAdmin = new User();
        superAdmin.setName("Super Admin");
        superAdmin.setEmail(email);
        superAdmin.setPassword(passwordEncoder.encode("password"));
        superAdmin.setRoles(Set.of(Role.SUPER_ADMIN));
        superAdmin.setSuperAdmin(true);
        superAdmin.setContactInfo("08123456789");
        superAdmin.setDescription("Platform-level administrator");

        userRepository.save(superAdmin);
        System.out.println("Super admin user seeded: " + email);
    }
}
