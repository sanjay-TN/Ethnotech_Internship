package com.expensetracker.dto;

import com.expensetracker.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotBlank String title,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotNull TransactionType transactionType,
        @NotNull LocalDate expenseDate,
        @NotBlank String category,
        String note
) {}
