package com.codingplatform.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String status;
    private int score;


    @ManyToOne
    private User user;

    @ManyToOne
    private Problem problem;
    private String language;
}