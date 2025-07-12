package org.jumia.services.user;

import org.jumia.dtos.requests.*;
import org.jumia.security.CurrentUserProvider;
import org.jumia.services.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.jumia.data.models.*;
import org.jumia.data.respositories.*;
import org.jumia.dtos.responses.*;
import org.jumia.exceptions.*;
import org.jumia.security.RoleValidator;
import org.jumia.security.JwtUtil;
import org.jumia.utility.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailSender emailSender;


    @Autowired
    private CurrentUserProvider currentUserProvider;


    /*

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use.");
        }

        User user = Mapper.toEntity(request);


        user.setPassword(passwordEncoder.encode(request.getPassword()));

//        if (request.getRoles() != null && request.getRoles().contains(Role.SUPER_ADMIN)) {
//            throw new IllegalArgumentException("Cannot register with SUPER_ADMIN role.");
//        }


        if (request.isSeller()) {
            user.setRoles(Set.of(Role.SELLER));
            if (request.getStoreName() == null || request.getStoreName().isBlank()) {
                throw new IllegalArgumentException("Store name is required for sellers.");
            }
            user.setStoreName(request.getStoreName());
        } else {
            user.setRoles(Set.of(Role.CUSTOMER));
        }

        if (user.getRoles().contains(Role.SUPER_ADMIN)) {
            throw new IllegalArgumentException("SUPER_ADMIN role cannot be assigned during registration.");
        }

        User savedUser = userRepository.save(user);

        return Mapper.toResponse(savedUser);

    }


    @Override
    public TokenDTO loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid credentials.");
        }

//        List<String> roles = RoleValidator.getRoleNames(user.getRoles());

        List<Role> roles = new ArrayList<>(user.getRoles());

        String token = jwtUtil.generateToken(user.getEmail(), roles);

        return new TokenDTO(token, System.currentTimeMillis() + jwtUtil.getExpirationTime());
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return Mapper.toResponse(user);
    }


    @Override
    public UserResponse getUserById(String id, String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));

        if (!RoleValidator.validateUserAccess(currentUser, id)) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        return Mapper.toResponse(user);
    }





     */

    //    @Override
//    public UserResponse loginUser(LoginUserRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new InvalidPasswordException("Invalid credentials.");
//        }
//        return Mapper.toResponse(user);
//    }


//    @Override
//    public UserResponse getUserById(String id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
//
////        if (!user.getId().equals(id) && !user.getRoles().contains(Role.ADMIN)) {
////            throw new AccessDeniedException("You are not authorized to access this resource.");
////        }
//
//        return Mapper.toResponse(user);
//
//    }



//    @Override
//    public UserResponse registerUser(RegisterUserRequest request) {
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new UserAlreadyExistsException("Email already in use.");
//        }
//
//        User user = Mapper.toEntity(request);
//        user.setContactInfo(request.getContactInfo());
//        user.setDescription(request.getDescription());
//
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
////        if (request.getRoles() != null && request.getRoles().contains(Role.SUPER_ADMIN)) {
////            throw new IllegalArgumentException("SUPER_ADMIN role cannot be assigned during registration.");
////        }
//
//
//
//        if (request.isSeller()) {
//            if (request.getStoreName() == null || request.getStoreName().isBlank()) {
//                throw new IllegalArgumentException("Store name is required for sellers.");
//            }
//            user.setRoles(Set.of(Role.SELLER));
//            user.setStoreName(request.getStoreName());
//        } else {
//            user.setRoles(Set.of(Role.CUSTOMER));
//        }
//
//        User savedUser = userRepository.save(user);
//
//        // ✅ Send welcome email
//        String subject = "🎉 Welcome to Sweet Sixteen Store";
//        String htmlContent = "<h1>Welcome " + savedUser.getName() + "!</h1>"
//                + "<p>Thank you for registering with us.</p>"
//                + "<p>We're excited to have you on board!</p>";
//
//        emailSender.send(savedUser.getEmail(), subject, htmlContent, true);
//
//
//        return Mapper.toResponse(savedUser);
//    }


@Override
public UserResponse registerUser(RegisterUserRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new UserAlreadyExistsException("Email already in use.");
    }

    User user = Mapper.toEntity(request);

    user.setContactInfo(request.getContactInfo());
    user.setDescription(request.getDescription());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Set<Role> assignedRoles;

    if (request.isSeller()) {
        if (request.getStoreName() == null || request.getStoreName().isBlank()) {
            throw new IllegalArgumentException("Store name is required for sellers.");
        }
        assignedRoles = Set.of(Role.SELLER);
        user.setStoreName(request.getStoreName());
    } else {
        assignedRoles = Set.of(Role.CUSTOMER);
    }

    if (assignedRoles.contains(Role.SUPER_ADMIN)) {
        throw new IllegalArgumentException("SUPER_ADMIN role cannot be assigned during registration.");
    }

    user.setRoles(assignedRoles);

    User savedUser = userRepository.save(user);
    sendWelcomeEmail(savedUser);

    return Mapper.toResponse(savedUser);
}



    @Override
    public TokenDTO loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid credentials.");
        }

        List<Role> roles = new ArrayList<>(user.getRoles());
        String token = jwtUtil.generateToken(user.getEmail(), roles);
        return new TokenDTO(token, System.currentTimeMillis() + jwtUtil.getExpirationTime());
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return Mapper.toResponse(user);
    }

    @Override
    public UserResponse getUserById(String id) {
        User currentUser = currentUserProvider.getAuthenticatedUser();

        if (!RoleValidator.validateUserAccess(currentUser, id)) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        return Mapper.toResponse(user);
    }

    @Override
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = currentUserProvider.getAuthenticatedUser();

        if (request.getName() != null) {
            user.setName(Mapper.formatFullName(request.getName()));
        }

        if (request.getStoreName() != null && RoleValidator.isSeller(user)) {
            user.setStoreName(Mapper.formatFullName(request.getStoreName()));
        }

        if (request.getContactInfo() != null) {
            user.setContactInfo(request.getContactInfo());
        }

        if (request.getDescription() != null) {
            user.setDescription(request.getDescription());
        }

        User updatedUser = userRepository.save(user);
        return Mapper.toResponse(updatedUser);
    }


//    @Override
//    public UserResponse updateProfile(UpdateProfileRequest request) {
//        User user = currentUserProvider.getAuthenticatedUser();
//
//        if (request.getName() != null) user.setName(request.getName());
//        if (request.getStoreName() != null && RoleValidator.isSeller(user)) {
//            user.setStoreName(request.getStoreName());
//        }
//        if (request.getContactInfo() != null) user.setContactInfo(request.getContactInfo());
//        if (request.getDescription() != null) user.setDescription(request.getDescription());
//
//        User updatedUser = userRepository.save(user);
//        return Mapper.toResponse(updatedUser);
//    }



    private void sendWelcomeEmail(User user) {
        String subject = "🎉 Welcome to Sweet Sixteen Store";
        String htmlContent = "<h1>Welcome " + user.getName() + "!</h1>"
                + "<p>Thank you for registering with us.</p>"
                + "<p>We're excited to have you on board!</p>";

        emailSender.send(user.getEmail(), subject, htmlContent, true);
    }



}
