package com.example.exbackend.repository;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    List<Exhibition> findByOrganizer(User organizer);
}