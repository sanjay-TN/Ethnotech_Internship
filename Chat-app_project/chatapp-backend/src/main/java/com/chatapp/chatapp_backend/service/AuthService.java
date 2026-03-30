package com.chatapp.chatapp_backend.service;

import com.chatapp.chatapp_backend.dto.AuthRequest;
import com.chatapp.chatapp_backend.dto.AuthResponse;
import com.chatapp.chatapp_backend.model.User;
import com.chatapp.chatapp_backend.repository.UserRepository;
import com.chatapp.chatapp_backend.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 🔹 REGISTER
    public AuthResponse register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        user.setOnline(false);
        userRepository.save(user);

        return AuthResponse.builder()
                .message("User Registered Successfully")
                .token("dummy-token")
                .userId(user.getId()) // 🔥 ADD
                .build();
    }

    // 🔹 LOGIN
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getPassword() == null || user.getPassword() == null ||
                !request.getPassword().equals(user.getPassword())) {

            throw new RuntimeException("Invalid credentials");
        }

        user.setOnline(true);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
                .message("Login Successful")
                .token(token)
                .userId(user.getId()) // 🔥 ADD
                .build();
    }
}