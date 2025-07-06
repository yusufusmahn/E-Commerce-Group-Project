package org.jumia.services.auth;

import org.jumia.data.models.User;
import org.jumia.data.respositories.UserRepository;
import org.jumia.dtos.requests.ForgotPasswordRequest;
import org.jumia.dtos.requests.ResetPasswordRequest;
import org.jumia.exceptions.ResourceNotFoundException;
import org.jumia.exceptions.TokenExpiredException;
import org.jumia.security.JwtUtil;
import org.jumia.services.email.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendResetLink(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // 1. Generate signed JWT reset token (short-lived, no roles needed)
        String resetToken = jwtUtil.generateToken(user.getEmail(), List.of());

        // 2. Store token and expiry (optional but extra safety)
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(System.currentTimeMillis() + jwtUtil.getExpirationTime());
        userRepository.save(user);

        // 3. Build reset link
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;

        // 4. HTML Email Body
        String htmlBody = "<p>Hello <strong>" + user.getName() + "</strong>,</p>"
                + "<p>You recently requested to reset your password. Click the button below to proceed:</p>"
                + "<a href=\"" + resetLink + "\" "
                + "style=\"display:inline-block;padding:10px 20px;background-color:#1976D2;color:#fff;text-decoration:none;border-radius:5px\">"
                + "Reset Password</a>"
                + "<p>This link will expire in 10 hours.</p>"
                + "<p>If you didn’t request this, you can safely ignore this email.</p>"
                + "<p><strong>Jumia Security Team</strong></p>";

        // 5. Send email
        emailSender.send(user.getEmail(), "🔐 Reset Your Password", htmlBody, true);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        String email = jwtUtil.extractUsername(request.getToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token."));

        // Validate token
        if (!request.getToken().equals(user.getResetToken())) {
            throw new TokenExpiredException("Invalid reset token.");
        }

        if (user.getResetTokenExpiry() < System.currentTimeMillis()) {
            throw new TokenExpiredException("Reset token has expired.");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(0L);
        userRepository.save(user);
    }
}
