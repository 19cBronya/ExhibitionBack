package com.example.exbackend.service;

import com.example.exbackend.model.Booth;
import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;

import java.util.List;

public interface BoothService {
    Booth assignBoothToExhibitor(Booth booth, Exhibition exhibition, User exhibitor);
    List<Booth> getAvailableBoothsForExhibition(Exhibition exhibition);
    List<Booth> getBoothsByExhibitor(User exhibitor);
}