package com.codingplatform.controller;

import com.codingplatform.dto.SubmissionRequest;
import com.codingplatform.dto.SubmissionResponse;
import com.codingplatform.security.JwtUtil;
import com.codingplatform.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public SubmissionResponse submit(@RequestBody SubmissionRequest request,
                                     HttpServletRequest httpRequest) {

        // 🔥 Extract token
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);

        // 🔥 Get email
        String email = jwtUtil.extractEmail(token);

        // 🔥 Call correct service method
        return submissionService.submit(request, email);
    }
}