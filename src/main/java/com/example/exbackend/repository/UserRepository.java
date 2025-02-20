package com.example.exbackend.repository;

import com.example.exbackend.model.User;
import com.example.exbackend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User>  findAllByRole(UserRole role);
}