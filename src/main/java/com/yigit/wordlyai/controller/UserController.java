package com.yigit.wordlyai.controller;

import com.yigit.wordlyai.dto.LoginRequest;
import com.yigit.wordlyai.dto.LoginResponse;
import com.yigit.wordlyai.dto.RegisterRequest;
import com.yigit.wordlyai.dto.UserResponse;
import com.yigit.wordlyai.entity.User;
import com.yigit.wordlyai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        User user = userService.register(request);
        UserResponse response = UserResponse.fromEntity(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        User user = userService.login(request);

        return new LoginResponse(
                "Login successful",
                UserResponse.fromEntity(user)
        );
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        User user = userService.findById(id);

        return UserResponse.fromEntity(user);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}
