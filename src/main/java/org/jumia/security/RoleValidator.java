// Updated RoleValidator
package org.jumia.security;

import org.jumia.data.models.Role;
import org.jumia.data.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleValidator {

    public static boolean isAdmin(User user) {
        return user.getRoles().contains(Role.ADMIN);
    }

    public static boolean isSeller(User user) {
        return user.getRoles().contains(Role.SELLER);
    }

    public static boolean isCustomer(User user) {
        return user.getRoles().contains(Role.CUSTOMER);
    }

    public static boolean isSuperAdmin(User user) {
        return user.getRoles().contains(Role.SUPER_ADMIN);
    }

    public static List<String> getRoleNames(Set<Role> roles) {
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.name());
        }
        return roleNames;
    }

    public static Set<Role> convertStringsToRoles(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            try {
                roles.add(Role.valueOf(roleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + roleName);
            }
        }
        return roles;
    }

    public static void addRole(User user, Role role) {
        user.getRoles().add(role);
    }

    public static void removeRole(User user, Role role) {
        user.getRoles().remove(role);
    }

    public static void validateSuperAdmin(User user) {
        if (!isSuperAdmin(user)) {
            throw new SecurityException("Unauthorized: Super Admin privileges required.");
        }
    }

    public static boolean validateUserAccess(User currentUser, String targetUserId) {
        return isSuperAdmin(currentUser) || isAdmin(currentUser) || currentUser.getId().equals(targetUserId);
    }

    public static boolean isNotCustomer(User user) {
        return !isCustomer(user);
    }

    public static boolean isNotSeller(User user) {
        return !isSeller(user);
    }


}
