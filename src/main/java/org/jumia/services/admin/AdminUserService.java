package org.jumia.services.admin;

import org.jumia.dtos.requests.DeleteUsersRequest;
import org.jumia.dtos.requests.UserSearchRequest;
import org.jumia.dtos.responses.*;

import java.util.List;

public interface AdminUserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String id);

    void deleteUserById(String id);

    void deleteUsers(DeleteUsersRequest request);

    List<UserResponse> searchUsers(UserSearchRequest userSearchRequest);
}


