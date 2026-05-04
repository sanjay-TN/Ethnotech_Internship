package com.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record DashboardResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal remainingBalance,
        BigDecimal currentMonthIncome,
        BigDecimal currentMonthExpenses,
        Map<String, BigDecimal> monthlySummary,
        List<CategorySpendingResponse> categorySpending,
        List<ExpenseResponse> recentExpenses
) {}
