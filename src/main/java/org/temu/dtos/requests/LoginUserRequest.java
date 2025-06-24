package org.temu.dtos.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

}
