package org.temu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.temu.dtos.requests.*;
import org.temu.dtos.responses.*;
import org.temu.exceptions.TemuException;
import org.temu.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        try {
            UserResponse response = userService.registerUser(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginUserRequest request) {
        try{
            UserResponse response = userService.loginUser(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") String id) {
        try{
            UserResponse response = userService.getUserById(id);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        }catch (TemuException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse> getUserById(@PathVariable("id") String id, Authentication authentication) {
//        try {
//            String currentUserEmail = authentication.getName();
//            UserResponse response = userService.getUserById(id, currentUserEmail);
//            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
//        } catch (TemuException e) {
//            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.FORBIDDEN);
//        }
//    }

}
