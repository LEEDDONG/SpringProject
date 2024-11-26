package com.example.demo.persistance;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);
}