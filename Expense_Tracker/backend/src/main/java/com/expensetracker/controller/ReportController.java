package com.expensetracker.controller;

import com.expensetracker.dto.ReportResponse;
import com.expensetracker.model.User;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.ReportService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final AuthService authService;
    private final ReportService reportService;

    @GetMapping("/reports/daily")
    public ReportResponse daily(HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return reportService.daily(user);
    }

    @GetMapping("/reports/weekly")
    public ReportResponse weekly(HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return reportService.weekly(user);
    }

    @GetMapping("/reports/monthly")
    public ReportResponse monthly(HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return reportService.monthly(user);
    }
}
