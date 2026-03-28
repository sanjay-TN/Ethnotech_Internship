package com.codingplatform.controller;

import com.codingplatform.model.Problem;
import com.codingplatform.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public List<Problem> getAllProblems() {
        return problemService.getAllProblems();
    }

    @GetMapping("/{id}")
    public Problem getProblem(@PathVariable Long id) {
        return problemService.getProblemById(id);
    }
}