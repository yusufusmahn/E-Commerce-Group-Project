package org.jumia.controllers.user;

import jakarta.validation.Valid;
import org.jumia.dtos.requests.UpdateProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jumia.dtos.requests.LoginUserRequest;
import org.jumia.dtos.requests.RegisterUserRequest;
import org.jumia.dtos.responses.TokenDTO;
import org.jumia.dtos.responses.UserResponse;
import org.jumia.services.user.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    /*


    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest request) {
        UserResponse userResponse = userService.registerUser(request);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody LoginUserRequest request) {
        TokenDTO tokenDTO = userService.loginUser(request);
        return ResponseEntity.ok(tokenDTO);
    }





    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal String currentUserEmail) {
        UserResponse userResponse = userService.getUserByEmail(currentUserEmail);
        return ResponseEntity.ok(userResponse);
    }

     */


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterUserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginUserRequest request) {
        TokenDTO token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponse updated = userService.updateProfile(request);
        return ResponseEntity.ok(updated);
    }

}
