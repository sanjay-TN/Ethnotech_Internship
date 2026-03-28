package com.codingplatform.service;

import com.codingplatform.model.TestCase;
import com.codingplatform.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;

    public TestCase addTestCase(TestCase testCase) {
        return testCaseRepository.save(testCase);
    }
}