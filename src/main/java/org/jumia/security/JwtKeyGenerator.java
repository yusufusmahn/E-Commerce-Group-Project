package org.jumia.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Base64 Secret Key:\n" + base64Key);
    }
}
