package com.codingplatform.service;

// package com.codingplatform.service;

import com.codingplatform.model.Problem;
import com.codingplatform.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public Problem addProblem(Problem problem) {
        return problemRepository.save(problem);
    }

    public List<Problem> getAllProblems() {
    return problemRepository.findAll();
}

public Problem getProblemById(Long id) {
    return problemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Problem not found"));
}
}