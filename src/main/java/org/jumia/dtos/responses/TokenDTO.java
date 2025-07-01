package org.jumia.dtos.responses;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;
    private long expirationTime;

    public TokenDTO(String token, long expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

}
