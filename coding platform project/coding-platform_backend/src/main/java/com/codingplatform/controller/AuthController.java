package com.codingplatform.controller;

import com.codingplatform.dto.LoginRequest;
import com.codingplatform.dto.LoginResponse;
import com.codingplatform.model.User;
import com.codingplatform.repository.UserRepository;
import com.codingplatform.security.JwtUtil;
import com.codingplatform.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import  org.springframework.web.bind.annotation.PostMapping;
import  org.springframework.web.bind.annotation.RequestBody;
import  org.springframework.web.bind.annotation.RequestMapping;
import  org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
     private final UserRepository userRepository;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    // @PostMapping("/login")
    // public LoginResponse login(@RequestBody LoginRequest request) {

    //     authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(
    //                     request.getEmail(),
    //                     request.getPassword()
    //             )
    //     );

    //     String token = jwtUtil.generateToken(request.getEmail());

    //     return new LoginResponse(token);
    // }

    @PostMapping("/login")
public LoginResponse login(@RequestBody LoginRequest request) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    User user = userRepository.findByEmail(request.getEmail()).get();

    String token = jwtUtil.generateToken(request.getEmail());

    return new LoginResponse(token, user.getRole().name());
}
}