package org.jumia.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Set;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private List<String> roles;
    private String storeName;
    private String contactInfo;
    private String description;


}
