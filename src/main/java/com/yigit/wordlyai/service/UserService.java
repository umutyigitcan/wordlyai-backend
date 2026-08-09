package com.yigit.wordlyai.service;

import com.yigit.wordlyai.dto.ChangePasswordRequest;
import com.yigit.wordlyai.dto.LoginRequest;
import com.yigit.wordlyai.dto.RegisterRequest;
import com.yigit.wordlyai.dto.UpdateProfileRequest;
import com.yigit.wordlyai.entity.User;
import com.yigit.wordlyai.exception.EmailAlreadyExistsException;
import com.yigit.wordlyai.exception.InvalidCredentialsException;
import com.yigit.wordlyai.exception.InvalidPasswordException;
import com.yigit.wordlyai.exception.UserNotFoundException;
import com.yigit.wordlyai.exception.UsernameAlreadyExistsException;
import com.yigit.wordlyai.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {
        String username = request.username().trim().toLowerCase(Locale.ROOT);
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(username, email, passwordHash);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(LoginRequest request) {
        String login = request.login().trim().toLowerCase(Locale.ROOT);

        User user = login.contains("@")
                ? userRepository.findByEmail(login)
                .orElseThrow(InvalidCredentialsException::new)
                : userRepository.findByUsername(login)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User changePassword(
            Long userId,
            ChangePasswordRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPasswordHash()
        )) {
            throw new InvalidPasswordException();
        }

        String newPasswordHash = passwordEncoder.encode(request.newPassword());
        user.changePasswordHash(newPasswordHash);

        return user;
    }

    @Transactional
    public User updateProfile(
            Long userId,
            UpdateProfileRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (request.biography() != null) {
            user.changeBiography(normalizeOptionalText(request.biography()));
        }

        if (request.profileImagePath() != null) {
            user.changeProfileImagePath(
                    normalizeOptionalText(request.profileImagePath())
            );
        }

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private String normalizeOptionalText(String value) {
        String normalizedValue = value.trim();
        return normalizedValue.isEmpty() ? null : normalizedValue;
    }
}
