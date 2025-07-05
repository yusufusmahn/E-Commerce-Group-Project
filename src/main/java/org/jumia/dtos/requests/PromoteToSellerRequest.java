package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class PromoteToSellerRequest {
    private String email; // Target user's email
    private String storeName;
}
