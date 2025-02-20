package com.example.exbackend.service;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule addSchedule(Schedule schedule, Exhibition exhibition);
    List<Schedule> getSchedulesByExhibition(Exhibition exhibition);
}