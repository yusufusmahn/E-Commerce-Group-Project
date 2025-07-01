package org.jumia.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToCartRequest {
    @NotBlank(message = "User ID is required.")
    private String userId;

    @NotBlank(message = "Product ID is required.")
    private String productId;

//    private String userEmail; // Add this field

}
