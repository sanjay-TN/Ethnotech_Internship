package com.expensetracker.service;

import com.expensetracker.dto.CategorySpendingResponse;
import com.expensetracker.dto.DashboardResponse;
import com.expensetracker.dto.ExpenseRequest;
import com.expensetracker.dto.ExpenseResponse;
import com.expensetracker.exception.ApiException;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.model.TransactionType;
import com.expensetracker.model.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public List<ExpenseResponse> search(User user, String query, String category, TransactionType type,
                                        LocalDate fromDate, LocalDate toDate, BigDecimal minAmount, BigDecimal maxAmount) {
        return expenseRepository.search(user.getId(), blankToNull(query), blankToNull(category), type, fromDate, toDate, minAmount, maxAmount)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ExpenseResponse create(User user, ExpenseRequest request) {
        Expense expense = new Expense();
        applyRequest(expense, request);
        expense.setUser(user);
        return toResponse(expenseRepository.save(expense));
    }

    public ExpenseResponse update(User user, Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .filter(existing -> existing.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Expense not found."));
        applyRequest(expense, request);
        return toResponse(expenseRepository.save(expense));
    }

    public void delete(User user, Long id) {
        Expense expense = expenseRepository.findById(id)
                .filter(existing -> existing.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Expense not found."));
        expenseRepository.delete(expense);
    }

    public DashboardResponse dashboard(User user) {
        List<Expense> all = expenseRepository.findByUserIdOrderByExpenseDateDesc(user.getId());
        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        BigDecimal monthlySalary = user.getMonthlyIncome() == null ? BigDecimal.ZERO : user.getMonthlyIncome();
        BigDecimal totalIncome = monthlySalary.add(sumByType(all, TransactionType.INCOME));
        BigDecimal totalExpenses = sumByType(all, TransactionType.EXPENSE);
        BigDecimal monthIncome = monthlySalary.add(sumByTypeBetween(all, TransactionType.INCOME, start, end));
        BigDecimal monthExpenses = sumByTypeBetween(all, TransactionType.EXPENSE, start, end);

        Map<String, BigDecimal> monthlySummary = buildSixMonthExpenseTrend(all);
        List<CategorySpendingResponse> categorySpending = buildCategorySpending(all, start, end);
        List<ExpenseResponse> recent = all.stream().limit(6).map(this::toResponse).toList();

        return new DashboardResponse(
                totalIncome,
                totalExpenses,
                monthIncome.subtract(monthExpenses),
                monthIncome,
                monthExpenses,
                monthlySummary,
                categorySpending,
                recent
        );
    }

    public List<String> categories() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .sorted()
                .toList();
    }

    public List<Expense> findBetween(User user, LocalDate from, LocalDate to) {
        return expenseRepository.findByUserIdAndExpenseDateBetweenOrderByExpenseDateAsc(user.getId(), from, to);
    }

    public ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getTransactionType(),
                expense.getExpenseDate(),
                expense.getCategory().getName(),
                expense.getNote()
        );
    }

    private void applyRequest(Expense expense, ExpenseRequest request) {
        Category category = categoryRepository.findByNameIgnoreCase(request.category())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Unknown category: " + request.category()));
        expense.setTitle(request.title().trim());
        expense.setAmount(request.amount());
        expense.setTransactionType(request.transactionType());
        expense.setExpenseDate(request.expenseDate());
        expense.setCategory(category);
        expense.setNote(request.note());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private BigDecimal sumByType(List<Expense> expenses, TransactionType type) {
        return expenses.stream()
                .filter(expense -> expense.getTransactionType() == type)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumByTypeBetween(List<Expense> expenses, TransactionType type, LocalDate from, LocalDate to) {
        return expenses.stream()
                .filter(expense -> expense.getTransactionType() == type)
                .filter(expense -> !expense.getExpenseDate().isBefore(from) && !expense.getExpenseDate().isAfter(to))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<String, BigDecimal> buildSixMonthExpenseTrend(List<Expense> expenses) {
        Map<String, BigDecimal> trend = new LinkedHashMap<>();
        YearMonth now = YearMonth.now();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = now.minusMonths(i);
            LocalDate from = month.atDay(1);
            LocalDate to = month.atEndOfMonth();
            String label = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            trend.put(label, sumByTypeBetween(expenses, TransactionType.EXPENSE, from, to));
        }
        return trend;
    }

    private List<CategorySpendingResponse> buildCategorySpending(List<Expense> expenses, LocalDate from, LocalDate to) {
        return expenses.stream()
                .filter(expense -> expense.getTransactionType() == TransactionType.EXPENSE)
                .filter(expense -> !expense.getExpenseDate().isBefore(from) && !expense.getExpenseDate().isAfter(to))
                .collect(Collectors.groupingBy(expense -> expense.getCategory().getName(), Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)))
                .entrySet()
                .stream()
                .map(entry -> new CategorySpendingResponse(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(CategorySpendingResponse::total).reversed())
                .toList();
    }
}
