package com.coding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

//package com.codingplatform.entity;

import jakarta.persistence.*;

@Entity
@Table(name="test_cases")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputData;

    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name="problem_id")
    @JsonBackReference
    private Problem problem;

    public TestCase(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
