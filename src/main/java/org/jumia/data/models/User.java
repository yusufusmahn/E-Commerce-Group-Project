package org.jumia.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private String storeName;


    private String contactInfo;
    private String description;
    private boolean superAdmin = false;
    private Role previousRole;
    private String resetToken;
    private Long resetTokenExpiry;



}


