package com.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record AIAnalysisResponse(
        BigDecimal predictedMonthlyExpense,
        List<String> smartAlerts,
        List<String> savingRecommendations,
        List<String> behaviorAnalysis,
        Map<String, BigDecimal> categoryTrends,
        List<String> unusualExpenses
) {}
