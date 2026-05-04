package com.expensetracker.repository;

import com.expensetracker.model.Expense;
import com.expensetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdOrderByExpenseDateDesc(Long userId);

    @Query("""
            select e from Expense e
            where e.user.id = :userId
              and (:query is null or lower(e.title) like lower(concat('%', :query, '%')) or lower(e.note) like lower(concat('%', :query, '%')))
              and (:category is null or lower(e.category.name) = lower(:category))
              and (:type is null or e.transactionType = :type)
              and (:fromDate is null or e.expenseDate >= :fromDate)
              and (:toDate is null or e.expenseDate <= :toDate)
              and (:minAmount is null or e.amount >= :minAmount)
              and (:maxAmount is null or e.amount <= :maxAmount)
            order by e.expenseDate desc, e.id desc
            """)
    List<Expense> search(
            @Param("userId") Long userId,
            @Param("query") String query,
            @Param("category") String category,
            @Param("type") TransactionType type,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("minAmount") java.math.BigDecimal minAmount,
            @Param("maxAmount") java.math.BigDecimal maxAmount
    );

    List<Expense> findByUserIdAndExpenseDateBetweenOrderByExpenseDateAsc(Long userId, LocalDate from, LocalDate to);
}
