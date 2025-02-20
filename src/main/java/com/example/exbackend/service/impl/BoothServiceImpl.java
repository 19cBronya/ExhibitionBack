package com.example.exbackend.service.impl;

import com.example.exbackend.exception.ResourceNotFoundException;
import com.example.exbackend.model.Booth;
import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;
import com.example.exbackend.repository.BoothRepository;
import com.example.exbackend.service.BoothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoothServiceImpl implements BoothService {

    @Autowired
    private BoothRepository boothRepository;

    @Override
    public Booth assignBoothToExhibitor(Booth booth, Exhibition exhibition, User exhibitor) {
        booth.setExhibition(exhibition);
        booth.setExhibitor(exhibitor);
        return boothRepository.save(booth);
    }

    @Override
    public List<Booth> getAvailableBoothsForExhibition(Exhibition exhibition) {
        return boothRepository.findByExhibitionAndExhibitorIsNull(exhibition);
    }

    @Override
    public List<Booth> getBoothsByExhibitor(User exhibitor) {
        return boothRepository.findByExhibitor(exhibitor);
    }
}