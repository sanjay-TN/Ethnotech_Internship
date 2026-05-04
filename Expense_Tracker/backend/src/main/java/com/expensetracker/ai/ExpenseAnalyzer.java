package com.expensetracker.ai;

import com.expensetracker.model.Expense;
import com.expensetracker.model.TransactionType;
import com.expensetracker.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExpenseAnalyzer {

    public List<String> detectOverspending(User user, List<Expense> expenses) {
        List<String> alerts = new ArrayList<>();
        BigDecimal currentMonthSpend = totalForMonth(expenses, YearMonth.now());
        BigDecimal income = user.getMonthlyIncome() == null ? BigDecimal.ZERO : user.getMonthlyIncome();

        if (income.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal spendRatio = currentMonthSpend.multiply(BigDecimal.valueOf(100)).divide(income, 2, RoundingMode.HALF_UP);
            if (spendRatio.compareTo(BigDecimal.valueOf(80)) > 0) {
                alerts.add("You have already spent " + spendRatio + "% of your monthly income.");
            } else if (spendRatio.compareTo(BigDecimal.valueOf(60)) > 0) {
                alerts.add("Your expenses crossed " + spendRatio + "% of your monthly income. Review flexible categories.");
            }
        }

        Map<String, BigDecimal> trends = analyzeCategoryTrends(expenses);
        trends.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(BigDecimal.valueOf(20)) > 0)
                .forEach(entry -> alerts.add("You are spending " + entry.getValue() + "% more on " + entry.getKey() + " this month."));

        if (alerts.isEmpty()) {
            alerts.add("Your current spending is within a healthy range based on available history.");
        }
        return alerts;
    }

    public BigDecimal predictMonthlyExpense(List<Expense> expenses) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate today = LocalDate.now();
        BigDecimal spentSoFar = totalForMonth(expenses, currentMonth);
        int elapsedDays = Math.max(1, today.getDayOfMonth());
        int daysInMonth = currentMonth.lengthOfMonth();

        BigDecimal pacePrediction = spentSoFar
                .divide(BigDecimal.valueOf(elapsedDays), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(daysInMonth));

        List<BigDecimal> previousMonths = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            BigDecimal monthlyTotal = totalForMonth(expenses, currentMonth.minusMonths(i));
            if (monthlyTotal.compareTo(BigDecimal.ZERO) > 0) {
                previousMonths.add(monthlyTotal);
            }
        }

        if (previousMonths.isEmpty()) {
            return pacePrediction.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal historicalAverage = previousMonths.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(previousMonths.size()), 2, RoundingMode.HALF_UP);

        return pacePrediction.multiply(BigDecimal.valueOf(0.65))
                .add(historicalAverage.multiply(BigDecimal.valueOf(0.35)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<String> suggestSavings(User user, List<Expense> expenses) {
        List<String> suggestions = new ArrayList<>();
        BigDecimal predicted = predictMonthlyExpense(expenses);
        BigDecimal income = user.getMonthlyIncome() == null ? BigDecimal.ZERO : user.getMonthlyIncome();

        if (income.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savingsPotential = income.subtract(predicted);
            if (savingsPotential.compareTo(income.multiply(BigDecimal.valueOf(0.2))) < 0) {
                suggestions.add("Aim to keep predicted spending under 80% of income by reducing flexible categories.");
            }
        }

        categoryTotals(expenses, YearMonth.now()).entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> {
                    BigDecimal reduction = entry.getValue().multiply(BigDecimal.valueOf(0.15)).setScale(2, RoundingMode.HALF_UP);
                    suggestions.add("Reduce " + entry.getKey() + " expenses by 15% to save about " + reduction + " this month.");
                });

        if (suggestions.isEmpty()) {
            suggestions.add("Add more expenses to receive personalized saving recommendations.");
        }
        return suggestions;
    }

    public Map<String, BigDecimal> analyzeCategoryTrends(List<Expense> expenses) {
        YearMonth current = YearMonth.now();
        Map<String, BigDecimal> currentTotals = categoryTotals(expenses, current);
        Map<String, BigDecimal> previousTotals = categoryTotals(expenses, current.minusMonths(1));
        Map<String, BigDecimal> trends = new LinkedHashMap<>();

        currentTotals.forEach((category, currentValue) -> {
            BigDecimal previousValue = previousTotals.getOrDefault(category, BigDecimal.ZERO);
            if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
                trends.put(category, currentValue.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);
            } else {
                BigDecimal change = currentValue.subtract(previousValue)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(previousValue, 2, RoundingMode.HALF_UP);
                trends.put(category, change);
            }
        });

        return trends.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public List<String> categorizeUnusualExpenses(List<Expense> expenses) {
        Map<String, List<Expense>> byCategory = expenseOnly(expenses).stream()
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getName()));

        List<String> unusual = new ArrayList<>();
        byCategory.forEach((category, categoryExpenses) -> {
            if (categoryExpenses.size() < 3) {
                return;
            }

            double average = categoryExpenses.stream().mapToDouble(expense -> expense.getAmount().doubleValue()).average().orElse(0);
            double variance = categoryExpenses.stream()
                    .mapToDouble(expense -> Math.pow(expense.getAmount().doubleValue() - average, 2))
                    .average()
                    .orElse(0);
            double standardDeviation = Math.sqrt(variance);
            double threshold = average + Math.max(standardDeviation * 1.5, average * 0.5);

            categoryExpenses.stream()
                    .filter(expense -> expense.getAmount().doubleValue() > threshold)
                    .max(Comparator.comparing(Expense::getAmount))
                    .ifPresent(expense -> unusual.add(expense.getTitle() + " looks unusual for " + category + " at " + expense.getAmount() + "."));
        });

        if (unusual.isEmpty()) {
            unusual.add("No unusual expenses detected from current spending history.");
        }
        return unusual;
    }

    public List<String> analyzeBehavior(List<Expense> expenses) {
        List<String> behavior = new ArrayList<>();
        BigDecimal predicted = predictMonthlyExpense(expenses);
        BigDecimal previousMonth = totalForMonth(expenses, YearMonth.now().minusMonths(1));

        if (previousMonth.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal change = predicted.subtract(previousMonth)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(previousMonth, 2, RoundingMode.HALF_UP);
            behavior.add("Predicted monthly spend is " + change + "% compared with last month.");
        }

        Map<String, BigDecimal> currentTotals = categoryTotals(expenses, YearMonth.now());
        currentTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> behavior.add(entry.getKey() + " is your highest spending category this month."));

        if (expenses.stream().filter(expense -> expense.getTransactionType() == TransactionType.EXPENSE).count() > 20) {
            behavior.add("You have many small transactions. Bundling purchases can reduce impulse spending.");
        }

        if (behavior.isEmpty()) {
            behavior.add("Your behavior analysis will become richer after a few weeks of expense history.");
        }
        return behavior;
    }

    private BigDecimal totalForMonth(List<Expense> expenses, YearMonth month) {
        return expenseOnly(expenses).stream()
                .filter(expense -> YearMonth.from(expense.getExpenseDate()).equals(month))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> categoryTotals(List<Expense> expenses, YearMonth month) {
        return expenseOnly(expenses).stream()
                .filter(expense -> YearMonth.from(expense.getExpenseDate()).equals(month))
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getName(), Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)));
    }

    private List<Expense> expenseOnly(List<Expense> expenses) {
        return expenses.stream()
                .filter(expense -> expense.getTransactionType() == TransactionType.EXPENSE)
                .toList();
    }
}
