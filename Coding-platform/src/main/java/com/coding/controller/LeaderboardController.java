package com.coding.controller;

//package com.codingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coding.dto.LeaderboardResponse;
import com.coding.service.LeaderboardService;



@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public List<LeaderboardResponse> getLeaderboard(){

        return leaderboardService.getLeaderboard();
    }
}
