package com.chatapp.chatapp_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String message;

    // 🔥 ADD THIS
    private Long userId;
}