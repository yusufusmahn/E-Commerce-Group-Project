package org.jumia.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jumia.data.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklist tokenBlacklist;



//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtUtil.extractUsername(jwt);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtUtil.validateToken(jwt, username)) {
//                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
//                        username, null, new ArrayList<>()));
//            }
//        }
//
//        chain.doFilter(request, response);
//    }


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//            username = jwtUtil.extractUsername(jwt);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtUtil.validateToken(jwt, username)) {
//                List<String> roles = jwtUtil.extractRoles(jwt);
//
//                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                for (String role : roles) {

    /// /                    authorities.add(new SimpleGrantedAuthority(role));
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//                }
//
//                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
//                        username, null, authorities));
//            }
//        }
//
//        chain.doFilter(request, response);
//    }
//
//}
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            if (tokenBlacklist.isTokenBlacklisted(jwt)) {
                throw new SecurityException("Token has been invalidated. Please login again.");
            }

            username = jwtUtil.extractUsername(jwt);
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, username)) {
                List<Role> roles = jwtUtil.extractRoles(jwt); // Extracting List<Role>

                // Convert roles to SimpleGrantedAuthority
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Role role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
                }

                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        username, null, authorities));
            }
        }

        chain.doFilter(request, response);
    }

}

