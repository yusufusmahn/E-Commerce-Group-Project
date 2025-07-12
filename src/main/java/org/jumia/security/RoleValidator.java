package org.jumia.security;

import org.jumia.data.models.Role;
import org.jumia.data.models.User;
import org.jumia.exceptions.AccessDeniedException;

import java.util.*;

public class RoleValidator {

    private static final Map<Role, Set<Role>> roleHierarchy = new HashMap<>();

    static {
        roleHierarchy.put(Role.SUPER_ADMIN, Set.of(Role.SUPER_ADMIN, Role.ADMIN, Role.SELLER, Role.CUSTOMER));
        roleHierarchy.put(Role.ADMIN, Set.of(Role.ADMIN, Role.SELLER, Role.CUSTOMER));
        roleHierarchy.put(Role.SELLER, Set.of(Role.SELLER, Role.CUSTOMER));
        roleHierarchy.put(Role.CUSTOMER, Set.of(Role.CUSTOMER));
    }


    public static boolean hasRole(User user, Role requiredRole) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) return false;

        for (Role userRole : user.getRoles()) {
            Set<Role> inheritedRoles = roleHierarchy.getOrDefault(userRole, Set.of(userRole));
            if (inheritedRoles.contains(requiredRole)) {
                return true;
            }
        }
        return false;
    }

    public static void validateAdminOrSuperAdmin(User user) {
        if (!user.getRoles().contains(Role.ADMIN) && !user.getRoles().contains(Role.SUPER_ADMIN)) {
            throw new AccessDeniedException("Only Admin or SuperAdmin can perform this action.");
        }
    }


    public static void validateRole(User user, Role requiredRole) {
        if (!hasRole(user, requiredRole)) {
            throw new SecurityException("Unauthorized: " + requiredRole.name() + " privileges required.");
        }
    }

    public static void validateOwnershipOrAdmin(User currentUser, String targetUserId) {
        if (!currentUser.getId().equals(targetUserId) && !isAdmin(currentUser) && !isSuperAdmin(currentUser)) {
            throw new SecurityException("Unauthorized access.");
        }
    }

    public static void validateSeller(User user) {
        validateRole(user, Role.SELLER);
    }

    public static void validateAdmin(User user) {
        validateRole(user, Role.ADMIN);
    }


    public static void addRole(User user, Role role) {
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
    }

    public static void removeRole(User user, Role role) {
        if (user.getRoles() != null) {
            user.getRoles().remove(role);
        }
    }

    public static boolean isSuperAdmin(User user) {
        return hasRole(user, Role.SUPER_ADMIN);
    }

    public static boolean isAdmin(User user) {
        return hasRole(user, Role.ADMIN);
    }

    public static boolean isSeller(User user) {
        return hasRole(user, Role.SELLER);
    }

    public static boolean isCustomer(User user) {
        return hasRole(user, Role.CUSTOMER);
    }

    public static List<String> getRoleNames(Set<Role> roles) {
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.name());
        }
        return roleNames;
    }

    public static void validateSelf(User user) {
        if (user == null) {
            throw new SecurityException("Unauthorized: no user authenticated.");
        }
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

    public static boolean validateUserAccess(User currentUser, String targetUserId) {
        return isSuperAdmin(currentUser) || isAdmin(currentUser) || currentUser.getId().equals(targetUserId);
    }

    public static boolean isNotCustomer(User user) {
        return !isCustomer(user);
    }

    public static boolean isNotSeller(User user) {
        return !isSeller(user);
    }
    public static void validateSuperAdmin(User user) {
        validateRole(user, Role.SUPER_ADMIN);
    }

    public static void validateCustomer(User user) {
        validateRole(user, Role.CUSTOMER);
    }

    public static Role getAnyRole(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalStateException("User has no assigned role.");
        }
        for (Role role : user.getRoles()) {
            return role; // returns the first (and only) one
        }
        throw new IllegalStateException("User has no assigned role.");
    }



//    public static void validateAnyRole(User user, Role... roles) {
//        for (Role role : roles) {
//            if (hasRole(user, role)) return;
//        }
//        throw new SecurityException("Unauthorized: Requires one of the roles " + Arrays.toString(roles));
//    }





//    public static boolean hasRole(User user, Role role) {
//        return user.getRoles() != null && user.getRoles().contains(role);
//    }



//    public static void validateSuperAdmin(User user) {
//        if (!isSuperAdmin(user)) {
//            throw new SecurityException("Unauthorized: Super Admin privileges required.");
//        }
//    }



//    public static void validateRole(User user, Role requiredRole) {
//        if (!hasRole(user, requiredRole)) {
//            throw new SecurityException("Unauthorized: " + requiredRole.name() + " privileges required.");
//        }
//    }





//    public static boolean hasRole(User user, Role role) {
//        return user.getRoles() != null && user.getRoles().contains(role);
//    }
//
//    public static void validateRole(User user, Role role) {
//        if (!hasRole(user, role)) {
//            throw new SecurityException("Unauthorized: " + role.name() + " privileges required.");
//        }
//    }




//
//    public static List<String> getRoleNames(Set<Role> roles) {
//        List<String> names = new ArrayList<>();
//        for (Role role : roles) {
//            names.add(role.name());
//        }
//        return names;
//    }



//


    /*
    package org.jumia.security;

import org.jumia.data.models.Role;
import org.jumia.data.models.User;

import java.util.*;

public class RoleValidator {

    private static final Map<Role, Set<Role>> roleHierarchy = new HashMap<>();

    static {
        roleHierarchy.put(Role.SUPER_ADMIN, Set.of(Role.SUPER_ADMIN, Role.ADMIN, Role.SELLER, Role.CUSTOMER));
        roleHierarchy.put(Role.ADMIN, Set.of(Role.ADMIN, Role.SELLER, Role.CUSTOMER));
        roleHierarchy.put(Role.SELLER, Set.of(Role.SELLER, Role.CUSTOMER));
        roleHierarchy.put(Role.CUSTOMER, Set.of(Role.CUSTOMER));
    }

    public static boolean hasRole(User user, Role requiredRole) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) return false;

        for (Role userRole : user.getRoles()) {
            Set<Role> inheritedRoles = roleHierarchy.getOrDefault(userRole, Set.of(userRole));
            if (inheritedRoles.contains(requiredRole)) {
                return true;
            }
        }
        return false;
    }

    public static void validateRole(User user, Role requiredRole) {
        if (!hasRole(user, requiredRole)) {
            throw new SecurityException("Unauthorized: " + requiredRole.name() + " privileges required.");
        }
    }

    public static void addRole(User user, Role role) {
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
    }

    public static void removeRole(User user, Role role) {
        if (user.getRoles() != null) {
            user.getRoles().remove(role);
        }
    }

    public static void validateSuperAdmin(User user) {
        validateRole(user, Role.SUPER_ADMIN);
    }

    public static boolean isSuperAdmin(User user) {
        return hasRole(user, Role.SUPER_ADMIN);
    }

    public static boolean isAdmin(User user) {
        return hasRole(user, Role.ADMIN);
    }

    public static boolean isSeller(User user) {
        return hasRole(user, Role.SELLER);
    }

    public static boolean isCustomer(User user) {
        return hasRole(user, Role.CUSTOMER);
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

    public static List<String> getRoleNames(Set<Role> roles) {
        List<String> names = new ArrayList<>();
        for (Role role : roles) {
            names.add(role.name());
        }
        return names;
    }

    public static Set<Role> convertStringsToRoles(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String name : roleNames) {
            try {
                roles.add(Role.valueOf(name.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + name);
            }

}

     */


}
