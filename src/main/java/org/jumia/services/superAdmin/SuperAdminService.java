package org.jumia.services.superAdmin;

import org.jumia.dtos.requests.PromoteToAdminRequest;
import org.jumia.dtos.requests.PromoteToSellerRequest;
import org.jumia.dtos.requests.RevokeAdminRoleRequest;
import org.jumia.dtos.requests.RevokeSellerRoleRequest;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.data.models.User;

public interface SuperAdminService {
//    UserResponse promoteToAdmin(PromoteToAdminRequest request, User currentUser);
//    UserResponse revokeAdminRole(RevokeAdminRoleRequest request, User currentUser);

    UserResponse promoteToAdmin(PromoteToAdminRequest request);

    UserResponse revokeAdminRole(RevokeAdminRoleRequest request);
    UserResponse promoteToSeller(PromoteToSellerRequest request);
    UserResponse revokeSellerRole(RevokeSellerRoleRequest request);


}
