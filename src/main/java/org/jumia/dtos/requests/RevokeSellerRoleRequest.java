package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class RevokeSellerRoleRequest {
    private String email; // Target user's email
}
