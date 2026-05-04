package com.expensetracker.service;

import com.expensetracker.dto.AuthResponse;
import com.expensetracker.dto.LoginRequest;
import com.expensetracker.dto.RegisterRequest;
import com.expensetracker.exception.ApiException;
import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String SESSION_USER_ID = "USER_ID";

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthResponse register(RegisterRequest request, HttpSession session) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered.");
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwordService.hash(request.password()));
        user.setMonthlyIncome(request.monthlyIncome() == null ? BigDecimal.ZERO : request.monthlyIncome());

        User savedUser = userRepository.save(user);
        session.setAttribute(SESSION_USER_ID, savedUser.getId());
        return toAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));

        if (!passwordService.matches(request.password(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }

        session.setAttribute(SESSION_USER_ID, user.getId());
        return toAuthResponse(user);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User requireCurrentUser(HttpSession session) {
        Object userId = session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Please login to continue.");
        }
        return userRepository.findById((Long) userId)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Session user no longer exists."));
    }

    public AuthResponse me(HttpSession session) {
        return toAuthResponse(requireCurrentUser(session));
    }

    private AuthResponse toAuthResponse(User user) {
        return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(), user.getMonthlyIncome());
    }
}
