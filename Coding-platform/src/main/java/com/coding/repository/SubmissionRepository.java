package com.coding.repository;

//package com.codingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coding.dto.LeaderboardResponse;
import com.coding.entity.Submission;

//import com.codingplatform.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long>{

    List<Submission> findByUserId(Long userId);
    
    @Query("""
    		SELECT new com.coding.dto.LeaderboardResponse(
    		u.username,
    		SUM(s.score))
    		FROM Submission s
    		JOIN s.user u
    		WHERE s.status = 'ACCEPTED'
    		GROUP BY u.username
    		ORDER BY SUM(s.score) DESC
    		""")
    		List<LeaderboardResponse> getLeaderboard();

}