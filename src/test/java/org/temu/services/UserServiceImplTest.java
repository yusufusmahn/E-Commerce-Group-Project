package org.temu.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.temu.data.models.User;
import org.temu.data.models.Role;
import org.temu.data.respositories.UserRepository;
import org.temu.dtos.requests.LoginUserRequest;
import org.temu.dtos.requests.RegisterUserRequest;
import org.temu.dtos.responses.UserResponse;
import org.temu.exceptions.InvalidPasswordException;
import org.temu.exceptions.ResourceNotFoundException;
import org.temu.exceptions.UserAlreadyExistsException;

import java.util.Set;


@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser_Success() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setName("niko's group");
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
        assertTrue(passwordEncoder.matches("password", userRepository.findByEmail("test@gmail.com").get().getPassword()));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        userService.registerUser(request);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    void testLoginUser_Success() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Set.of(Role.CUSTOMER));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password");

        UserResponse response = userService.loginUser(request);

        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Set.of(Role.CUSTOMER));
        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("wrongPassword");

        assertThrows(InvalidPasswordException.class, () -> userService.loginUser(request));
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(Set.of(Role.CUSTOMER));
        User savedUser = userRepository.save(user);

        UserResponse response = userService.getUserById(savedUser.getId());

        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
    }

    @Test
    void testGetUserById_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById("nonexistent-id"));
    }
}
