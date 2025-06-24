package org.temu.utility;


import org.temu.data.models.*;
import org.temu.dtos.requests.*;
import org.temu.dtos.responses.*;

public class Mapper {

    public static User toEntity(RegisterUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
//        if (request.getRoles() != null) {
//            user.setRoles(request.getRoles());
//        }
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

}
