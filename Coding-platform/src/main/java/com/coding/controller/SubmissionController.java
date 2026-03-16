package com.coding.controller;

//package com.codingplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coding.dto.SubmissionRequest;
import com.coding.dto.SubmissionResponse;
import com.coding.service.SubmissionService;



@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping
    public SubmissionResponse submitSolution(@RequestBody SubmissionRequest request){

        return submissionService.submitSolution(
                request.getUserId(),
                request.getProblemId(),
                request.getCode());
    }
}
