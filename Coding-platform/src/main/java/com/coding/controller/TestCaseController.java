package com.coding.controller;

//package com.codingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coding.entity.TestCase;
import com.coding.service.TestCaseService;



@RestController
@RequestMapping("/api/testcases")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @PostMapping("/{problemId}")
    public TestCase addTestCase(@PathVariable Long problemId,
                                @RequestBody TestCase testCase){

        return testCaseService.addTestCase(problemId, testCase);
    }

    @GetMapping("/problem/{problemId}")
    public List<TestCase> getTestCases(@PathVariable Long problemId){

        return testCaseService.getTestCases(problemId);
    }
}
