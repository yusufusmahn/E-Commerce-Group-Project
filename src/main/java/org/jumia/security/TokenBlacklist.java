// src/main/java/org/jumia/security/TokenBlacklist.java
package org.jumia.security;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklistToken(String token, long expiryTime) {
        blacklist.put(token, expiryTime);
    }

    public boolean isTokenBlacklisted(String token) {
        if (!blacklist.containsKey(token)) return false;

        // Auto-remove expired entries
        long expiryTime = blacklist.get(token);
        if (expiryTime < System.currentTimeMillis()) {
            blacklist.remove(token);
            return false;
        }

        return true;
    }
}
