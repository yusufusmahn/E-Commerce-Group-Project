package org.jumia.services.auth;

import org.jumia.dtos.requests.ForgotPasswordRequest;
import org.jumia.dtos.requests.ResetPasswordRequest;

public interface PasswordService {
    void sendResetLink(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
