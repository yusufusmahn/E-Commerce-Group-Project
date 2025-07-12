package org.jumia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/super-admin/**").hasRole("SUPER_ADMIN")
                                .requestMatchers("/api/users/register", "/api/users/login", "/api/shop/**").permitAll()
                                .requestMatchers("/api/auth/forgot-password", "/api/auth/reset-password").permitAll()
                                .requestMatchers("/api/users/**").authenticated()
                                .requestMatchers("/api/customer/cart/merge").authenticated()
                                .requestMatchers("/api/auth/logout").authenticated()
                                .requestMatchers("/api/payments/initiate").authenticated()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/api/admin/**").hasRole("SUPER_ADMIN")
                                .requestMatchers("/api/seller/**").hasRole("SELLER")
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()
//                        .requestMatchers("/api/seller/**").hasAuthority("ROLE_SELLER")

                                .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                );
        return http.build();
    }


        @Bean
        public RoleHierarchy roleHierarchy() {
            RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
            roleHierarchy.setHierarchy("""
            ROLE_SUPER_ADMIN > ROLE_ADMIN
            ROLE_ADMIN > ROLE_SELLER
            ROLE_SELLER > ROLE_CUSTOMER
        """);
            return roleHierarchy;
        }



}
