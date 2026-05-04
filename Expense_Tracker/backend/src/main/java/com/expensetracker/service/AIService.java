package com.expensetracker.service;

import com.expensetracker.ai.ExpenseAnalyzer;
import com.expensetracker.dto.AIAnalysisResponse;
import com.expensetracker.model.AIInsight;
import com.expensetracker.model.Expense;
import com.expensetracker.model.User;
import com.expensetracker.repository.AIInsightRepository;
import com.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {

    private final ExpenseAnalyzer expenseAnalyzer;
    private final ExpenseRepository expenseRepository;
    private final AIInsightRepository aiInsightRepository;

    public AIAnalysisResponse analyze(User user) {
        List<Expense> expenses = expenseRepository.findByUserIdOrderByExpenseDateDesc(user.getId());
        BigDecimal predicted = expenseAnalyzer.predictMonthlyExpense(expenses);
        List<String> alerts = expenseAnalyzer.detectOverspending(user, expenses);
        List<String> suggestions = expenseAnalyzer.suggestSavings(user, expenses);
        List<String> behavior = expenseAnalyzer.analyzeBehavior(expenses);
        List<String> unusual = expenseAnalyzer.categorizeUnusualExpenses(expenses);

        saveInsight(user, "PREDICTION", "Predicted monthly spending is " + predicted + ".", predicted);
        alerts.forEach(alert -> saveInsight(user, "ALERT", alert, null));
        suggestions.stream().limit(2).forEach(suggestion -> saveInsight(user, "SAVING", suggestion, null));

        return new AIAnalysisResponse(
                predicted,
                alerts,
                suggestions,
                behavior,
                expenseAnalyzer.analyzeCategoryTrends(expenses),
                unusual
        );
    }

    private void saveInsight(User user, String type, String message, BigDecimal score) {
        AIInsight insight = new AIInsight();
        insight.setUser(user);
        insight.setInsightType(type);
        insight.setMessage(message);
        insight.setScore(score);
        aiInsightRepository.save(insight);
    }
}
