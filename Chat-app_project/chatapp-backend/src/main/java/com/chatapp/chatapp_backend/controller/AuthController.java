package com.chatapp.chatapp_backend.controller;

// package com.chatapp.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatapp.chatapp_backend.dto.AuthRequest;
import com.chatapp.chatapp_backend.dto.AuthResponse;
import com.chatapp.chatapp_backend.model.User;
import com.chatapp.chatapp_backend.repository.UserRepository;
import com.chatapp.chatapp_backend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
public String logout(@RequestParam String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setOnline(false);
    userRepository.save(user);

    return "Logged out successfully";
}
}
