package com.example.exbackend.repository;

import com.example.exbackend.model.Product;
import com.example.exbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByExhibitor(User exhibitor);
}