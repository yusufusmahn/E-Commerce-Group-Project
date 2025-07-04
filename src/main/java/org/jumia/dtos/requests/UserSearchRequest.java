package org.jumia.dtos.requests;

import lombok.Data;

@Data
public class UserSearchRequest {
    private String email;
    private String role;


}
