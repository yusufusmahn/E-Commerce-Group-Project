package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class PasswordResetConfirmRequest {
    private String token;
    private String newPassword;
}