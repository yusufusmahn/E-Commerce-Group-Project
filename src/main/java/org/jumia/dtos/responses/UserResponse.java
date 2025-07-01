package org.jumia.dtos.responses;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private Set<String> roles; // Add this field


}
