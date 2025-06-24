package org.temu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.temu.data.models.*;
import org.temu.data.respositories.UserRepository;
import org.temu.dtos.requests.LoginUserRequest;
import org.temu.dtos.requests.RegisterUserRequest;
import org.temu.dtos.responses.UserResponse;
import org.temu.exceptions.InvalidPasswordException;
import org.temu.exceptions.*;
import org.temu.utility.Mapper;

import java.util.Set;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(RegisterUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use.");
        }
        User user = Mapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRoles(Set.of(Role.CUSTOMER));

        User savedUser = userRepository.save(user);
        return Mapper.toResponse(savedUser);
    }

    @Override
    public UserResponse loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid credentials.");
        }
        return Mapper.toResponse(user);
    }


    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

//        if (!user.getId().equals(id) && !user.getRoles().contains(Role.ADMIN)) {
//            throw new AccessDeniedException("You are not authorized to access this resource.");
//        }

        return Mapper.toResponse(user);
    }


//    @Override
//    public UserResponse getUserById(String id, String currentUserEmail) {
//        User currentUser = userRepository.findByEmail(currentUserEmail)
//                .orElseThrow(() -> new ResourceNotFoundException("Current user not found."));
//
//        if (!currentUser.getRoles().contains(Role.ADMIN) && !currentUser.getId().equals(id)) {
//            throw new AccessDeniedException("You are not authorized to access this resource.");
//        }
//
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
//
//        return Mapper.toResponse(user);
//    }


}
