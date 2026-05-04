package com.expensetracker.dto;

import java.math.BigDecimal;

public record AuthResponse(
        Long id,
        String fullName,
        String email,
        BigDecimal monthlyIncome
) {}
