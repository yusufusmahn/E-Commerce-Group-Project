package org.jumia.services.superAdmin;

import org.jumia.dtos.requests.PromoteToAdminRequest;
import org.jumia.dtos.requests.RevokeAdminRoleRequest;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.data.models.User;

public interface SuperAdminService {
    UserResponse promoteToAdmin(PromoteToAdminRequest request, User currentUser);
    UserResponse revokeAdminRole(RevokeAdminRoleRequest request, User currentUser);
}
