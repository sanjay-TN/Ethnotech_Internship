package com.expensetracker.service;

import com.expensetracker.dto.CategorySpendingResponse;
import com.expensetracker.dto.ReportResponse;
import com.expensetracker.model.Expense;
import com.expensetracker.model.TransactionType;
import com.expensetracker.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExpenseService expenseService;

    public ReportResponse daily(User user) {
        LocalDate today = LocalDate.now();
        return buildReport(user, "Daily", today, today, DateTimeFormatter.ofPattern("MMM d"));
    }

    public ReportResponse weekly(User user) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.with(DayOfWeek.MONDAY);
        LocalDate to = today.with(DayOfWeek.SUNDAY);
        return buildReport(user, "Weekly", from, to, DateTimeFormatter.ofPattern("EEE"));
    }

    public ReportResponse monthly(User user) {
        YearMonth month = YearMonth.now();
        return buildReport(user, "Monthly", month.atDay(1), month.atEndOfMonth(), DateTimeFormatter.ofPattern("MMM d"));
    }

    private ReportResponse buildReport(User user, String period, LocalDate from, LocalDate to, DateTimeFormatter formatter) {
        List<Expense> expenses = expenseService.findBetween(user, from, to);
        BigDecimal income = salaryForPeriod(user, from, to).add(sum(expenses, TransactionType.INCOME));
        BigDecimal spent = sum(expenses, TransactionType.EXPENSE);
        Map<String, BigDecimal> trend = buildTrend(expenses, from, to, formatter);

        List<CategorySpendingResponse> categories = expenses.stream()
                .filter(expense -> expense.getTransactionType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getName(), Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)))
                .entrySet()
                .stream()
                .map(entry -> new CategorySpendingResponse(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(CategorySpendingResponse::total).reversed())
                .toList();

        return new ReportResponse(period, income, spent, income.subtract(spent), trend, categories);
    }

    private Map<String, BigDecimal> buildTrend(List<Expense> expenses, LocalDate from, LocalDate to, DateTimeFormatter formatter) {
        Map<String, BigDecimal> trend = new LinkedHashMap<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            LocalDate current = date;
            BigDecimal total = expenses.stream()
                    .filter(expense -> expense.getTransactionType() == TransactionType.EXPENSE)
                    .filter(expense -> expense.getExpenseDate().equals(current))
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            trend.put(current.format(formatter), total);
        }
        return trend;
    }

    private BigDecimal sum(List<Expense> expenses, TransactionType type) {
        return expenses.stream()
                .filter(expense -> expense.getTransactionType() == type)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal salaryForPeriod(User user, LocalDate from, LocalDate to) {
        BigDecimal monthlyIncome = user.getMonthlyIncome() == null ? BigDecimal.ZERO : user.getMonthlyIncome();
        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        YearMonth month = YearMonth.from(from);
        if (from.equals(month.atDay(1)) && to.equals(month.atEndOfMonth())) {
            return monthlyIncome;
        }

        long daysInRange = java.time.temporal.ChronoUnit.DAYS.between(from, to) + 1;
        return monthlyIncome
                .multiply(BigDecimal.valueOf(daysInRange))
                .divide(BigDecimal.valueOf(month.lengthOfMonth()), 2, java.math.RoundingMode.HALF_UP);
    }
}
