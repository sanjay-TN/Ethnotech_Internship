package com.coding.service;

///package com.codingplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.dto.LeaderboardResponse;
import com.coding.repository.SubmissionRepository;



@Service
public class LeaderboardService {

    @Autowired
    private SubmissionRepository submissionRepository;

    public List<LeaderboardResponse> getLeaderboard(){

        List<LeaderboardResponse> leaderboard = submissionRepository.getLeaderboard();

        int rank = 1;

        for(LeaderboardResponse entry : leaderboard){
            entry.setRank(rank++);
        }

        return leaderboard;
    }
}

