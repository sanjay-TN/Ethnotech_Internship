package com.coding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coding.entity.Problem;



public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
