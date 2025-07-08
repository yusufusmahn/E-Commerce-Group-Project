package org.jumia.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.jumia.dtos.requests.*;
import org.jumia.services.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private LogoutService logoutService;


    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendResetLink(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordService.sendResetLink(request);
        return ResponseEntity.ok("Reset link sent to your email.");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful.");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        logoutService.logout(request);
        return ResponseEntity.ok("Logged out successfully.");
    }

}
