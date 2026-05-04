package com.expensetracker.dto;

import com.expensetracker.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
        Long id,
        String title,
        BigDecimal amount,
        TransactionType transactionType,
        LocalDate expenseDate,
        String category,
        String note
) {}
