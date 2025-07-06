package org.jumia.services.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.jumia.security.JwtUtil;
import org.jumia.security.TokenBlacklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {

    private static final Logger logger = LoggerFactory.getLogger(LogoutServiceImpl.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Override
    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header during logout");
            throw new IllegalArgumentException("Authorization token is missing or invalid.");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        long expiry = jwtUtil.extractExpiration(token).getTime();

        tokenBlacklist.blacklistToken(token, expiry);

        logger.info("✅ User '{}' has logged out successfully. Token blacklisted.", username);
    }
}
