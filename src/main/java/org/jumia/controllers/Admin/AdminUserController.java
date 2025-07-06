package org.jumia.controllers.Admin;

import org.jumia.dtos.requests.DeleteUsersRequest;
import org.jumia.dtos.requests.UserSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jumia.dtos.responses.*;
import org.jumia.services.admin.AdminUserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = adminUserService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("email") String email) {
        UserResponse user = adminUserService.getUserById(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("email") String email) {
        adminUserService.deleteUserById(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/users/delete")
    public ResponseEntity<String> deleteUsers(@RequestBody DeleteUsersRequest request) {
        adminUserService.deleteUsers(request);
        return ResponseEntity.ok("Users deleted successfully");
    }


    @PostMapping("/users/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestBody UserSearchRequest request) {
        List<UserResponse> result = adminUserService.searchUsers(request);
        return ResponseEntity.ok(result);
    }




}
