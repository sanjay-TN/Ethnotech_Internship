package com.expensetracker.repository;

import com.expensetracker.model.AIInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIInsightRepository extends JpaRepository<AIInsight, Long> {
    List<AIInsight> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
