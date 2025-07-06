package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
}