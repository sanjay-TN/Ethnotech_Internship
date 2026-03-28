package com.codingplatform.controller;

import com.codingplatform.model.Problem;
import com.codingplatform.model.TestCase;
import com.codingplatform.service.ProblemService;
import com.codingplatform.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProblemService problemService;
    private final TestCaseService testCaseService;

    @PostMapping("/problem")
    public Problem addProblem(@RequestBody Problem problem) {
        return problemService.addProblem(problem);
    }

    @PostMapping("/testcase")
    public String addTestCase(@RequestBody TestCase testCase) {
    testCaseService.addTestCase(testCase);
    return "Test case added successfully";
}
}