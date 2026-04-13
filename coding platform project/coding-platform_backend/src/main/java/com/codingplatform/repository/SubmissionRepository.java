package com.codingplatform.repository;

import com.codingplatform.model.Submission;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s.user.email, SUM(s.score) FROM Submission s GROUP BY s.user.email ORDER BY SUM(s.score) DESC")

    List<Object[]> getLeaderboard();
}