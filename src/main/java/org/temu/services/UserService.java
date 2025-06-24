package org.temu.services;


import org.temu.dtos.requests.*;
import org.temu.dtos.responses.*;

public interface UserService {
    UserResponse registerUser(RegisterUserRequest request);
    UserResponse loginUser(LoginUserRequest request);
    UserResponse getUserById(String id);
    //    UserResponse getUserById(String id, String email);
}
