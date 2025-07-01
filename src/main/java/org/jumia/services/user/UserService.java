package org.jumia.services.user;


import org.jumia.dtos.requests.*;
import org.jumia.dtos.responses.*;

public interface UserService {
    UserResponse registerUser(RegisterUserRequest request);
//    UserResponse loginUser(LoginUserRequest request);
//    UserResponse getUserById(String id);
    //    UserResponse getUserById(String id, String email);
    TokenDTO loginUser(LoginUserRequest request);
    UserResponse getUserByEmail(String email);
    UserResponse getUserById(String id, String currentUserEmail); // New method

}
