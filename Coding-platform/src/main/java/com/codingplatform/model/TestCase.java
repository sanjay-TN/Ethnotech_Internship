package com.codingplatform.model;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String input;
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
