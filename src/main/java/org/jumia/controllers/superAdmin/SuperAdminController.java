package org.jumia.controllers.superAdmin;

import org.jumia.dtos.requests.PromoteToSellerRequest;
import org.jumia.dtos.requests.RevokeSellerRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.jumia.data.models.User;
import org.jumia.dtos.requests.PromoteToAdminRequest;
import org.jumia.dtos.requests.RevokeAdminRoleRequest;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.security.CurrentUserProvider;
import org.jumia.services.superAdmin.SuperAdminService;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

//    @PostMapping("/promote-to-admin")
//    public ResponseEntity<UserResponse> promoteToAdmin(@RequestBody PromoteToAdminRequest request) {
//        User currentUser = currentUserProvider.getCurrentUser()
//                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
//
//        UserResponse response = superAdminService.promoteToAdmin(request, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @PostMapping("/revoke-admin-role")
//    public ResponseEntity<UserResponse> revokeAdminRole(@RequestBody RevokeAdminRoleRequest request) {
//        User currentUser = currentUserProvider.getCurrentUser()
//                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
//
//        UserResponse response = superAdminService.revokeAdminRole(request, currentUser);
//        return ResponseEntity.ok(response);
//    }


//    @PostMapping("/promote-to-admin")
//    public ResponseEntity<UserResponse> promoteToAdmin(
//            @RequestBody PromoteToAdminRequest request,
//            @AuthenticationPrincipal User currentUser) {
//        if (currentUser == null) {
//            throw new SecurityException("User must be authenticated to perform this action.");
//        }
//        System.out.println("Current User Roles: " + currentUser.getRoles());
//
//        UserResponse response = superAdminService.promoteToAdmin(request, currentUser);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @PostMapping("/revoke-admin-role")
//    public ResponseEntity<UserResponse> revokeAdminRole(
//            @RequestBody RevokeAdminRoleRequest request,
//            @AuthenticationPrincipal User currentUser) {
//        UserResponse response = superAdminService.revokeAdminRole(request, currentUser);
//        return ResponseEntity.ok(response);
//    }



    @PostMapping("/promote-to-admin")
    public ResponseEntity<UserResponse> promoteToAdmin(
            @RequestBody PromoteToAdminRequest request) {
        UserResponse response = superAdminService.promoteToAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke-admin-role")
    public ResponseEntity<UserResponse> revokeAdminRole(
            @RequestBody RevokeAdminRoleRequest request) {
        UserResponse response = superAdminService.revokeAdminRole(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promote-to-seller")
    public ResponseEntity<UserResponse> promoteToSeller(
            @RequestBody PromoteToSellerRequest request) {
        UserResponse response = superAdminService.promoteToSeller(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/revoke-seller-role")
    public ResponseEntity<UserResponse> revokeSellerRole(@RequestBody RevokeSellerRoleRequest request) {
        UserResponse response = superAdminService.revokeSellerRole(request);
        return ResponseEntity.ok(response);
    }


}
