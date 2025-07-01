package org.jumia.services.superAdmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.requests.PromoteToAdminRequest;
import org.jumia.dtos.requests.RevokeAdminRoleRequest;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.exceptions.UserAlreadyAdminException;
import org.jumia.exceptions.UserNotAdminException;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse promoteToAdmin(PromoteToAdminRequest request, User currentUser) {
        RoleValidator.validateSuperAdmin(currentUser);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (RoleValidator.isAdmin(user)) {
            throw new UserAlreadyAdminException("User is already an admin.");
        }

        RoleValidator.addRole(user, Role.ADMIN);
        userRepository.save(user);

        return Mapper.mapToUserResponse(user);
    }

    @Override
    public UserResponse revokeAdminRole(RevokeAdminRoleRequest request, User currentUser) {
        RoleValidator.validateSuperAdmin(currentUser);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!RoleValidator.isAdmin(user)) {
            throw new UserNotAdminException("User is not an admin.");
        }

        RoleValidator.removeRole(user, Role.ADMIN);
        userRepository.save(user);

        return Mapper.mapToUserResponse(user);
    }
}
