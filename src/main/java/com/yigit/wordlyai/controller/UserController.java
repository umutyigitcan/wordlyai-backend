package com.yigit.wordlyai.controller;

import com.yigit.wordlyai.dto.ChangePasswordRequest;
import com.yigit.wordlyai.dto.LoginRequest;
import com.yigit.wordlyai.dto.LoginResponse;
import com.yigit.wordlyai.dto.RegisterRequest;
import com.yigit.wordlyai.dto.UpdateProfileRequest;
import com.yigit.wordlyai.dto.UserResponse;
import com.yigit.wordlyai.entity.User;
import com.yigit.wordlyai.service.JwtService;
import com.yigit.wordlyai.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final JwtService jwtService;

    public UserController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
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
        String token = jwtService.generateToken(user);

        return new LoginResponse(
                "Login successful",
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                UserResponse.fromEntity(user)
        );
    }

    @PatchMapping("/change-password")
    public UserResponse changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        Long userId = getUserId(jwt);
        User user = userService.changePassword(userId, request);

        return UserResponse.fromEntity(user);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Long userId = getUserId(jwt);
        User user = userService.findById(userId);

        return UserResponse.fromEntity(user);
    }

    @PatchMapping("/me")
    public UserResponse updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        Long userId = getUserId(jwt);
        User user = userService.updateProfile(userId, request);

        return UserResponse.fromEntity(user);
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

    private Long getUserId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}
