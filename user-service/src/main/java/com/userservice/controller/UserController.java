package com.userservice.controller;

import com.userservice.DTO.RegisterRequest;
import com.userservice.DTO.UserResponse;
import com.userservice.model.User;
import com.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
        User user = this.userService.register(registerRequest.username(), registerRequest.password());
        UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/account")
    public String  account(){
        System.out.println("account success");
        return "account";
    }
}
