// src/main/java/org/jumia/services/auth/LogoutService.java
package org.jumia.services.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface LogoutService {
    void logout(HttpServletRequest request);
}
