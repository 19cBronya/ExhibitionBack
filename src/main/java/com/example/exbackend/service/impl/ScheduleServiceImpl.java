package com.example.exbackend.service.impl;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.Schedule;
import com.example.exbackend.repository.ScheduleRepository;
import com.example.exbackend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public Schedule addSchedule(Schedule schedule, Exhibition exhibition) {
        schedule.setExhibition(exhibition);
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getSchedulesByExhibition(Exhibition exhibition) {
        return scheduleRepository.findByExhibition(exhibition);
    }
}