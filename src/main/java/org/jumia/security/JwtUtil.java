package org.jumia.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.jumia.data.models.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key SECRET_KEY;

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;


    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.SECRET_KEY = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {
        if (token == null || !token.contains(".")) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


//    public String generateToken(String username, List<String> roles) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("roles", roles)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
//                .compact();
//
//    }

    public String generateToken(String username, List<Role> roles) {
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.name());
        }

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roleNames)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }



    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);

        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }


    public long getExpirationTime() {
        return EXPIRATION_TIME;
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

//    public List<String> extractRoles(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.get("roles", List.class);
//    }

    public List<Role> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roleStrings = claims.get("roles", List.class);

        List<Role> roles = new ArrayList<>();
        for (String roleString : roleStrings) {
            try {
                roles.add(Role.valueOf(roleString.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role in token: " + roleString);
            }
        }
        return roles;
    }





}
