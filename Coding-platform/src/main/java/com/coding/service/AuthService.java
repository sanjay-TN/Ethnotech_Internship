package com.coding.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.dto.AuthRequest;
import com.coding.entity.User;
import com.coding.repository.UserRepository;



@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(AuthRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public String login(AuthRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isPresent()) {

            if(user.get().getPassword().equals(request.getPassword())) {
                return "Login Successful";
            }
        }

        return "Invalid Credentials";
    }
}