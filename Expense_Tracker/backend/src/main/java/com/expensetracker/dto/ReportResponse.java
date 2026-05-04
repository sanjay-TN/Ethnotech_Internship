package com.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ReportResponse(
        String period,
        BigDecimal income,
        BigDecimal expenses,
        BigDecimal balance,
        Map<String, BigDecimal> trend,
        List<CategorySpendingResponse> categoryBreakdown
) {}
