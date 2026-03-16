package com.coding.service;

//package com.codingplatform.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.dto.SubmissionResponse;
import com.coding.entity.Problem;
import com.coding.entity.Submission;
import com.coding.entity.User;
import com.coding.repository.ProblemRepository;
import com.coding.repository.SubmissionRepository;
import com.coding.repository.UserRepository;



@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;
    
    @Autowired
    private EvaluationService evaluationService;

    public SubmissionResponse submitSolution(Long userId, Long problemId, String code){

        User user = userRepository.findById(userId).orElseThrow();
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        Submission submission = new Submission();

        submission.setUser(user);
        submission.setProblem(problem);
        submission.setCode(code);
        submission.setSubmittedAt(LocalDateTime.now());

        submission.setStatus("PENDING");
        submission.setScore(0);

        Submission savedSubmission = submissionRepository.save(submission);

        // Simulated output (for demo)
        String userOutput = "9";

        int passedTests = evaluationService.evaluateSubmission(savedSubmission, userOutput);

        if(passedTests > 0){
            savedSubmission.setStatus("ACCEPTED");
            savedSubmission.setScore(passedTests * 10);
        } else {
            savedSubmission.setStatus("WRONG_ANSWER");
            savedSubmission.setScore(0);
        }

        submissionRepository.save(savedSubmission);

        SubmissionResponse response = new SubmissionResponse();

        response.setSubmissionId(savedSubmission.getId());
        response.setUsername(user.getUsername());
        response.setProblemTitle(problem.getTitle());
        response.setStatus(savedSubmission.getStatus());
        response.setScore(savedSubmission.getScore());
        response.setSubmittedAt(savedSubmission.getSubmittedAt());

        return response;
    }
}
