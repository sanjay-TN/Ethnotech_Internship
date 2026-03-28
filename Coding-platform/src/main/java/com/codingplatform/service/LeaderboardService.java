package com.codingplatform.service;

import com.codingplatform.dto.LeaderboardResponse;
import com.codingplatform.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final SubmissionRepository submissionRepository;

    public List<LeaderboardResponse> getLeaderboard() {

        return submissionRepository.getLeaderboard()
                .stream()
                .map(obj -> new LeaderboardResponse(
                        (String) obj[0],
                        (Long) obj[1]
                ))
                .collect(Collectors.toList());
    }
}