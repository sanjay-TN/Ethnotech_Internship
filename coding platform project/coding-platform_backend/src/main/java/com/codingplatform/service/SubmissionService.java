package com.codingplatform.service;

import com.codingplatform.dto.SubmissionRequest;
import com.codingplatform.dto.SubmissionResponse;
import com.codingplatform.model.*;
import com.codingplatform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final CodeExecutionService codeExecutionService;

    public SubmissionResponse submit(SubmissionRequest request, String email) {

        // 🔥 Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Get problem
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        // 🔥 Create submission
        Submission submission = new Submission();
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setStatus("PENDING");
        submission.setScore(0);

        Submission saved = submissionRepository.save(submission);

        // 🔥 Get test cases
        var testCases = testCaseRepository.findByProblemId(request.getProblemId());

        int passed = 0;

        for (TestCase tc : testCases) {

            String output = codeExecutionService.execute(
                    request.getCode(),
                    request.getLanguage(),
                    tc.getInput()
            );

            // 🔥 DEBUG (important)
            System.out.println("INPUT: " + tc.getInput());
            System.out.println("EXPECTED: [" + tc.getExpectedOutput() + "]");
            System.out.println("ACTUAL: [" + output + "]");
            System.out.println("-------------------");

            // 🔥 FINAL CORRECT COMPARISON
            if (output.trim().equalsIgnoreCase(tc.getExpectedOutput().trim())) {
                passed++;
            }
        }

        int total = testCases.size();

        // 🔥 Avoid division by zero
        if (total == 0) {
            saved.setStatus("FAIL");
            saved.setScore(0);
        } else {
            if (passed == total) {
                saved.setStatus("PASS");
            } else {
                saved.setStatus("FAIL");
            }

            saved.setScore((passed * 100) / total);
        }

        submissionRepository.save(saved);

        return SubmissionResponse.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .score(saved.getScore())
                .problemId(problem.getId())
                .build();
    }
}