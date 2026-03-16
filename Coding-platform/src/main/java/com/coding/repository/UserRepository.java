package com.coding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coding.entity.User;



public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

}