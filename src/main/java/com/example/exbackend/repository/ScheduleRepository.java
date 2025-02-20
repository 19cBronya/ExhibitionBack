package com.example.exbackend.repository;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByExhibition(Exhibition exhibition);
}