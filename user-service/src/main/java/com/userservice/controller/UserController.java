package com.userservice.controller;

import com.userservice.DTO.LoginRequest;
import com.userservice.DTO.LoginResponse;
import com.userservice.DTO.RegisterRequest;
import com.userservice.DTO.UserResponse;
import com.userservice.model.User;
import com.userservice.service.JwtService;
import com.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    public UserController(UserService userService,  AuthenticationManager authenticationManager,  JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user.getUsername(), user.getId());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}
