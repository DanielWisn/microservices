package com.userservice.controller;

import com.userservice.DTO.RegisterRequest;
import com.userservice.DTO.UserResponse;
import com.userservice.model.User;
import com.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername()))
                .toList();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
        User user = this.userService.register(registerRequest.username(), registerRequest.password());
        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
        System.out.println(userResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/account")
    public ResponseEntity<UserResponse> account(Authentication  authentication){
        return ResponseEntity.ok( new UserResponse(null, authentication.getName()));
    }
}
