package com.example.exbackend.repository;

import com.example.exbackend.model.Booth;
import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BoothRepository extends JpaRepository<Booth, Long> {
    List<Booth> findByExhibitionAndExhibitorIsNull(Exhibition exhibition);
    List<Booth> findByExhibitor(User exhibition);
}