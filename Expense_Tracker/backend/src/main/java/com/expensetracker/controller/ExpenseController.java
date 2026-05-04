package com.expensetracker.controller;

import com.expensetracker.dto.DashboardResponse;
import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.dto.ExpenseResponse;
import com.expensetracker.model.TransactionType;
import com.expensetracker.model.User;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final AuthService authService;
    private final ExpenseService expenseService;

    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpenses(
            HttpSession session,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount
    ) {
        User user = authService.requireCurrentUser(session);
        return expenseService.search(user, query, category, type, fromDate, toDate, minAmount, maxAmount);
    }

    @PostMapping("/expenses")
    public ExpenseResponse addExpense(@Valid @RequestBody ExpenseRequest request, HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return expenseService.create(user, request);
    }

    @PutMapping("/expenses/{id}")
    public ExpenseResponse updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request, HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return expenseService.update(user, id, request);
    }

    @DeleteMapping("/expenses/{id}")
    public Map<String, String> deleteExpense(@PathVariable Long id, HttpSession session) {
        User user = authService.requireCurrentUser(session);
        expenseService.delete(user, id);
        return Map.of("message", "Expense deleted successfully.");
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(HttpSession session) {
        User user = authService.requireCurrentUser(session);
        return expenseService.dashboard(user);
    }

    @GetMapping("/categories")
    public List<String> categories() {
        return expenseService.categories();
    }
}
