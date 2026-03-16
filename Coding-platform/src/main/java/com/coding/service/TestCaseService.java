package com.coding.service;

//package com.codingplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.entity.Problem;
import com.coding.entity.TestCase;
import com.coding.repository.ProblemRepository;
import com.coding.repository.TestCaseRepository;


@Service
public class TestCaseService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private ProblemRepository problemRepository;

    public TestCase addTestCase(Long problemId, TestCase testCase) {

        Problem problem = problemRepository.findById(problemId).orElseThrow();

        testCase.setProblem(problem);

        return testCaseRepository.save(testCase);
    }

    public List<TestCase> getTestCases(Long problemId){
        return testCaseRepository.findByProblemId(problemId);
    }

}