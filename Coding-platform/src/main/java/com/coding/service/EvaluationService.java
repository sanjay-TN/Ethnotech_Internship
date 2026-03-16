package com.coding.service;

//package com.codingplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.entity.Submission;
import com.coding.entity.TestCase;
import com.coding.repository.TestCaseRepository;



@Service
public class EvaluationService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    public int evaluateSubmission(Submission submission, String userOutput){

        Long problemId = submission.getProblem().getId();

        List<TestCase> testCases = testCaseRepository.findByProblemId(problemId);

        int passedTests = 0;

        for(TestCase testCase : testCases){

            String expected = testCase.getExpectedOutput();

            if(expected == null){
                continue;
            }

            if(userOutput.trim().equals(expected.trim())){
                passedTests++;
            }
        }

        return passedTests;
    }
}
