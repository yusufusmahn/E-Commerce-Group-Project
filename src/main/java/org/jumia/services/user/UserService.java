package org.jumia.services.user;


import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;

public interface UserService {


    UserResponse registerUser(RegisterUserRequest request);

    TokenDTO loginUser(LoginUserRequest request);

    UserResponse getUserByEmail(String email);

    UserResponse getUserById(String id);
    UserResponse updateProfile(UpdateProfileRequest request);

}