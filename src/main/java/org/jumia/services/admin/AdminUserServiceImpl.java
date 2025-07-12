package org.jumia.services.admin;

import org.jumia.dtos.requests.DeleteUsersRequest;
import org.jumia.dtos.requests.UserSearchRequest;
import org.jumia.security.CurrentUserProvider;
import org.jumia.security.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.jumia.data.models.*;
import org.jumia.data.respositories.*;
import org.jumia.dtos.responses.*;
import org.jumia.exceptions.*;
import org.jumia.utility.Mapper;

import java.util.*;
import java.util.logging.Logger;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    private static final Logger logger = Logger.getLogger(AdminUserServiceImpl.class.getName());


    private void validateAdminRole() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User adminUser = userRepository.findByEmail(Mapper.cleanEmail(email))
                .orElseThrow(() -> new SecurityException("Authenticated admin not found"));

        if (adminUser.getRoles() == null || !adminUser.getRoles().contains(Role.ADMIN)) {
            throw new SecurityException("Access denied: ADMIN role required.");
        }
    }


//    @Override
//    public List<UserResponse> getAllUsers() {
//        validateAdminRole();
//        List<User> users = userRepository.findAll();
//        return Mapper.mapUserListToResponseList(users);
//    }

//    @Override
//    public UserResponse getUserById(String email) {
//        validateAdminRole();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + email));
//        return Mapper.toResponse(user);
//    }

//    @Override
//    public void deleteUserById(String email) {
//        validateAdminRole();
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + email));
//        userRepository.delete(user);
//    }

//    @Override
//    public void deleteUsers(List<String> emails) {
//        for (String email : emails) {
//            User user = userRepository.findByEmail(email).orElse(null);
//            if (user != null) {
//                userRepository.delete(user);
//            }
//        }
//    }

    //

    @Override
    public void deleteUsers(DeleteUsersRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(currentUser, Role.ADMIN);

        List<String> emails = request.getEmails();
        for (String email : emails) {
            User user = userRepository.findByEmail(Mapper.cleanEmail(email))
                    .orElse(null);
            if (user != null) {
                userRepository.delete(user);
            }
        }
    }



//        @Override
//    public List<UserResponse> searchUsers(String email, String role) {
//        validateAdminRole();
//        List<User> users;
//
//        if (email != null && role != null) {
//            Role searchRole = Role.valueOf(role.toUpperCase());
//            users = userRepository.findByEmailContainingIgnoreCaseAndRolesContaining(email, searchRole);
//        } else if (email != null) {
//            users = userRepository.findByEmailContainingIgnoreCase(email);
//        } else if (role != null) {
//            Role searchRole = Role.valueOf(role.toUpperCase());
//            users = userRepository.findByRolesContaining(searchRole);
//        } else {
//            users = userRepository.findAll();
//        }
//
//        return Mapper.mapUserListToResponseList(users);
//    }







    // ✅ Get all users
    @Override
    public List<UserResponse> getAllUsers() {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(currentUser, Role.ADMIN);

        List<User> users = userRepository.findAll();
        return Mapper.mapUserListToResponseList(users);
    }

    @Override
    public UserResponse getUserById(String email) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(currentUser, Role.ADMIN);

        User user = userRepository.findByEmail(Mapper.cleanEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return Mapper.toResponse(user);
    }

    @Override
    public void deleteUserById(String email) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateRole(currentUser, Role.ADMIN);

        User user = userRepository.findByEmail(Mapper.cleanEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        userRepository.delete(user);
    }
//
//    // ✅ Delete multiple users
//    @Override
//    public void deleteUsers(List<String> emails) {
//        User currentUser = currentUserProvider.getAuthenticatedUser();
//        RoleValidator.validateRole(currentUser, Role.ADMIN);
//
//        for (String email : emails) {
//            userRepository.findByEmail(email).ifPresent(userRepository::delete);
//        }
//    }
//
//    // ✅ Search users by email and/or role
//    @Override
//    public List<UserResponse> searchUsers(String email, String role) {
//        User currentUser = currentUserProvider.getAuthenticatedUser();
//        RoleValidator.validateRole(currentUser, Role.ADMIN);
//
//        List<User> users;
//
//        if (email != null && role != null) {
//            Role searchRole = Role.valueOf(role.toUpperCase());
//            users = userRepository.findByEmailContainingIgnoreCaseAndRolesContaining(email, searchRole);
//        } else if (email != null) {
//            users = userRepository.findByEmailContainingIgnoreCase(email);
//        } else if (role != null) {
//            Role searchRole = Role.valueOf(role.toUpperCase());
//            users = userRepository.findByRolesContaining(searchRole);
//        } else {
//            users = userRepository.findAll();
//        }
//
//        return Mapper.mapUserListToResponseList(users);
//    }


@Override
public List<UserResponse> searchUsers(UserSearchRequest request) {
    User currentUser = currentUserProvider.getAuthenticatedUser();
    RoleValidator.validateRole(currentUser, Role.ADMIN);

    String email = request.getEmail();
    String role = request.getRole();

    if (email != null) {
        email = email.trim().toLowerCase();
    }

    List<User> users;

    if (email != null && role != null) {
        Role searchRole = parseRoleSafely(role);
        users = userRepository.findByEmailContainingIgnoreCaseAndRolesContaining(email, searchRole);

    } else if (email != null) {
        users = userRepository.findByEmailContainingIgnoreCase(email);

    } else if (role != null) {
        Role searchRole = parseRoleSafely(role);
        users = userRepository.findByRolesContaining(searchRole);

    } else {
        users = userRepository.findAll();
    }

    return Mapper.mapUserListToResponseList(users);
}


    private Role parseRoleSafely(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (JumiaException e) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }






}
