package com.coding.repository;

//package com.codingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coding.entity.TestCase;

//import com.codingplatform.entity.TestCase;

public interface TestCaseRepository extends JpaRepository<TestCase, Long>{

    List<TestCase> findByProblemId(Long problemId);

}
