package com.expensetracker.controller;

import com.expensetracker.dto.AIAnalysisResponse;
import com.expensetracker.model.User;
import com.expensetracker.service.AIService;
import com.expensetracker.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AuthService authService;
    private final AIService aiService;

    @GetMapping("/ai/analyze")
    public AIAnalysisResponse analyze(HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return aiService.analyze(user);
    }
}
