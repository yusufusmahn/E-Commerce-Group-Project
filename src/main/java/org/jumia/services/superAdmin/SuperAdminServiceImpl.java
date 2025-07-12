package org.jumia.services.superAdmin;

import org.jumia.dtos.requests.*;
import org.jumia.exceptions.EntityNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.services.audit.GeneralAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.exceptions.UserAlreadyAdminException;
import org.jumia.exceptions.UserNotAdminException;
import org.jumia.security.RoleValidator;
import org.jumia.utility.Mapper;

import java.util.HashSet;
import java.util.Set;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Autowired
    private GeneralAuditLogService auditLogService;

//    @Override
//    public UserResponse promoteToAdmin(PromoteToAdminRequest request, User currentUser) {
//        RoleValidator.validateSuperAdmin(currentUser);
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
//
//        if (user.getRoles() == null) {
//            user.setRoles(new HashSet<>());
//        }
//
//        if (RoleValidator.isAdmin(user)) {
//            throw new UserAlreadyAdminException("User is already an admin.");
//        }
////        if (!RoleValidator.isAdmin(user)) {
////            throw new UserNotAdminException("User is not an admin.");
////        }
//
//        RoleValidator.addRole(user, Role.ADMIN);
//        userRepository.save(user);
//
//        return Mapper.mapToUserResponse(user);
//    }
//
//
//    @Override
//    public UserResponse revokeAdminRole(RevokeAdminRoleRequest request, User currentUser) {
//        RoleValidator.validateSuperAdmin(currentUser);
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
//
//        if (user.getRoles() == null) {
//            user.setRoles(new HashSet<>());
//        }
//
//        if (!RoleValidator.isAdmin(user)) {
//            throw new UserNotAdminException("User is not an admin.");
//        }
//        if (RoleValidator.isAdmin(user)) {
//            throw new UserAlreadyAdminException("User is already an admin.");
//        }
//
//        RoleValidator.removeRole(user, Role.ADMIN);
//        userRepository.save(user);
//
//        return Mapper.mapToUserResponse(user);
//    }


//public UserResponse promoteToAdmin(PromoteToAdminRequest request) {
//    // 1. Get current authenticated user
//    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//    User currentUser = userRepository.findByEmail(currentUsername)
//            .orElseThrow(() -> new SecurityException("Current user not found"));
//
//    validateSuperAdmin(currentUser); // Validate that caller is SUPER_ADMIN
//
//    // 2. Get target user to promote
//    User targetUser = userRepository.findByEmail(request.getEmail())
//            .orElseThrow(() -> new EntityNotFoundException("User with email " + request.getEmail() + " not found."));
//
//    if (targetUser.getRoles().contains(Role.ADMIN)) {
//        throw new IllegalStateException("User is already an admin.");
//    }
//
//    // 3. Promote to ADMIN
//    Set<Role> updatedRoles = new HashSet<>(targetUser.getRoles());
//    updatedRoles.add(Role.ADMIN);
//    targetUser.setRoles(updatedRoles);
//
//    userRepository.save(targetUser);
//    return Mapper.mapToUserResponse(targetUser);
//}
//
//    public UserResponse revokeAdminRole(RevokeAdminRoleRequest request) {
//        // 1. Get current authenticated user
//        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
//        User currentUser = userRepository.findByEmail(currentUsername)
//                .orElseThrow(() -> new SecurityException("Current user not found"));
//
//        validateSuperAdmin(currentUser); // Validate that caller is SUPER_ADMIN
//
//        // 2. Get target user whose admin role will be revoked
//        User targetUser = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new EntityNotFoundException("User with email " + request.getEmail() + " not found."));
//
//        if (!targetUser.getRoles().contains(Role.ADMIN)) {
//            throw new IllegalStateException("User is not an admin.");
//        }
//
//        // 3. Remove ADMIN role
//        Set<Role> updatedRoles = new HashSet<>(targetUser.getRoles());
//        updatedRoles.remove(Role.ADMIN);
//        targetUser.setRoles(updatedRoles);
//
//        userRepository.save(targetUser);
//        return Mapper.mapToUserResponse(targetUser);
//    }
//
//    private void validateSuperAdmin(User user) {
//        if (user.getRoles() == null || !user.getRoles().contains(Role.SUPER_ADMIN)) {
//            throw new SecurityException("Unauthorized: Super Admin privileges required.");
//        }
//    }






/*

public UserResponse promoteToAdmin(PromoteToAdminRequest request) {
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    User currentUser = userRepository.findByEmail(currentUsername)
            .orElseThrow(() -> new SecurityException("Current user not found"));

    validateSuperAdmin(currentUser);

    User targetUser = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getEmail()));

    // If already admin, do nothing
    if (targetUser.getRoles().contains(Role.ADMIN)) {
        throw new IllegalStateException("User is already an admin.");
    }

    // Save current (non-admin) role as previousRole if not already set
    if (targetUser.getPreviousRole() == null) {
        for (Role role : targetUser.getRoles()) {
            if (role != Role.ADMIN) {
                targetUser.setPreviousRole(role);
                break;
            }
        }
    }

    // Promote: replace roles with ADMIN
    Set<Role> adminRoleSet = new HashSet<>();
    adminRoleSet.add(Role.ADMIN);
    targetUser.setRoles(adminRoleSet);

    userRepository.save(targetUser);
    return Mapper.toResponse(targetUser);

}


    public UserResponse revokeAdminRole(RevokeAdminRoleRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new SecurityException("Current user not found"));

        validateSuperAdmin(currentUser);

        User targetUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getEmail()));

        if (!targetUser.getRoles().contains(Role.ADMIN)) {
            throw new IllegalStateException("User is not an admin.");
        }

        // Restore previous role
        Role previousRole = targetUser.getPreviousRole();
        if (previousRole == null || previousRole == Role.ADMIN) {
            System.out.println("⚠️ Previous role invalid or missing. Defaulting to CUSTOMER.");
            previousRole = Role.CUSTOMER;
        }

        Set<Role> restoredRole = new HashSet<>();
        restoredRole.add(previousRole);
        targetUser.setRoles(restoredRole);
        targetUser.setPreviousRole(null);

        userRepository.save(targetUser);
        return Mapper.toResponse(targetUser);
    }

 */








    @Override
    public UserResponse promoteToAdmin(PromoteToAdminRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(currentUser);

        User targetUser = userRepository.findByEmail(Mapper.cleanEmail(request.getEmail()))

                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getEmail()));

        if (targetUser.getRoles().contains(Role.ADMIN)) {
            throw new IllegalStateException("User is already an admin.");
        }

        if (targetUser.getPreviousRole() == null) {
            for (Role role : targetUser.getRoles()) {
                if (role != Role.ADMIN) {
                    targetUser.setPreviousRole(role);
                    break;
                }
            }
        }

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);
        targetUser.setRoles(adminRoles);

        userRepository.save(targetUser);

        auditLogService.log(
                currentUser.getId(),
                Role.SUPER_ADMIN,
                "PROMOTE_TO_ADMIN",
                "USER",
                targetUser.getId(),
                "Promoted user to ADMIN: " + targetUser.getEmail()
        );

        return Mapper.toResponse(targetUser);


    }

    @Override
    public UserResponse revokeAdminRole(RevokeAdminRoleRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(currentUser);

        User targetUser = userRepository.findByEmail(Mapper.cleanEmail(request.getEmail()))

                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getEmail()));

        if (!targetUser.getRoles().contains(Role.ADMIN)) {
            throw new IllegalStateException("User is not an admin.");
        }

        Role previousRole = targetUser.getPreviousRole();
        if (previousRole == null || previousRole == Role.ADMIN) {
            System.out.println("Previous role invalid or missing. Defaulting to CUSTOMER.");
            previousRole = Role.CUSTOMER;
        }

        Set<Role> restored = new HashSet<>();
        restored.add(previousRole);
        targetUser.setRoles(restored);
        targetUser.setPreviousRole(null);

        userRepository.save(targetUser);

        auditLogService.log(
                currentUser.getId(),
                Role.SUPER_ADMIN,
                "REVOKE_ADMIN",
                "USER",
                targetUser.getId(),
                "Revoked ADMIN role, restored to: " + previousRole.name()
        );

        return Mapper.toResponse(targetUser);
    }


    @Override
    public UserResponse promoteToSeller(PromoteToSellerRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(currentUser);

        User targetUser = userRepository.findByEmail(Mapper.cleanEmail(request.getEmail()))

                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getEmail()));

        if (targetUser.getRoles().contains(Role.SELLER)) {
            throw new IllegalStateException("User is already a seller.");
        }

        if (request.getStoreName() == null || request.getStoreName().isBlank()) {
            throw new IllegalArgumentException("Store name is required for promotion to seller.");
        }

        if (targetUser.getPreviousRole() == null) {
            for (Role role : targetUser.getRoles()) {
                if (role != Role.SELLER) {
                    targetUser.setPreviousRole(role);
                    break;
                }
            }
        }

        targetUser.setRoles(Set.of(Role.SELLER));
        targetUser.setStoreName(Mapper.formatFullName(request.getStoreName()));
//        targetUser.setStoreName(request.getStoreName());

        userRepository.save(targetUser);

        auditLogService.log(
                currentUser.getId(),
                Role.SUPER_ADMIN,
                "PROMOTE_TO_SELLER",
                "USER",
                targetUser.getId(),
                "Promoted user to SELLER: " + targetUser.getEmail()
        );

        return Mapper.toResponse(targetUser);
    }

    @Override
    public UserResponse revokeSellerRole(RevokeSellerRoleRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(currentUser);

        User targetUser = userRepository.findByEmail(Mapper.cleanEmail(request.getEmail()))

                .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getEmail()));

        if (!targetUser.getRoles().contains(Role.SELLER)) {
            throw new IllegalStateException("User is not a seller.");
        }

        Role previousRole = targetUser.getPreviousRole();
        if (previousRole == null || previousRole == Role.SELLER) {
            System.out.println("Previous role invalid or missing. Defaulting to CUSTOMER.");
            previousRole = Role.CUSTOMER;
        }

        targetUser.setRoles(Set.of(previousRole));
        targetUser.setPreviousRole(null);
        targetUser.setStoreName(null);

        userRepository.save(targetUser);

        auditLogService.log(
                currentUser.getId(),
                Role.SUPER_ADMIN,
                "REVOKE_SELLER",
                "USER",
                targetUser.getId(),
                "Revoked SELLER role, restored to: " + previousRole.name()
        );


        return Mapper.toResponse(targetUser);
    }


    @Override
    public UserResponse deleteUser(DeleteUserRequest request) {
        User currentUser = currentUserProvider.getAuthenticatedUser();
        RoleValidator.validateSuperAdmin(currentUser);

        String email = request.getEmail();

        User targetUser = userRepository.findByEmail(Mapper.cleanEmail(request.getEmail()))

                .orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

        userRepository.delete(targetUser);

        auditLogService.log(
                currentUser.getId(),
                Role.SUPER_ADMIN,
                "DELETE_USER",
                "USER",
                targetUser.getId(),
                "Deleted user with email: " + email
        );

        return Mapper.toResponse(targetUser);
    }



//    private void validateSuperAdmin(User user) {
//        if (user.getRoles() == null || !user.getRoles().contains(Role.SUPER_ADMIN)) {
//            throw new SecurityException("Unauthorized: Super Admin privileges required.");
//        }
//
//    }
}
