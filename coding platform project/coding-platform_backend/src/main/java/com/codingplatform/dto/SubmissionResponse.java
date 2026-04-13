package com.codingplatform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmissionResponse {

    private Long id;
    private String status;
    private int score;
    private Long problemId;
}