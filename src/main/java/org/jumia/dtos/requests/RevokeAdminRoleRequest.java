package org.jumia.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RevokeAdminRoleRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;
}
