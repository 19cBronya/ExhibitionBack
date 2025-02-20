package com.example.exbackend.controller;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.Schedule;
import com.example.exbackend.model.User;
import com.example.exbackend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule, @RequestParam Long exhibitionId) {
        User organizer = getCurrentUser();
        Exhibition exhibition = getExhibitionById(exhibitionId); // Assume this is implemented
        Schedule addedSchedule = scheduleService.addSchedule(schedule, exhibition);
        return ResponseEntity.ok(addedSchedule);
    }

    @GetMapping("/for-exhibition/{exhibitionId}")
    public ResponseEntity<List<Schedule>> getSchedulesForExhibition(@PathVariable Long exhibitionId) {
        Exhibition exhibition = getExhibitionById(exhibitionId); // Assume this is implemented
        List<Schedule> schedules = scheduleService.getSchedulesByExhibition(exhibition);
        return ResponseEntity.ok(schedules);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Exhibition getExhibitionById(Long exhibitionId) {
        // Implement logic to retrieve exhibition by ID from repository
        // For simplicity, assume it's implemented in a service method
        return null;
    }
}