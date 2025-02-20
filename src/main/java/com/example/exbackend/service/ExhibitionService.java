package com.example.exbackend.service;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;

import java.util.List;

public interface ExhibitionService {
    Exhibition createExhibition(Exhibition exhibition, User organizer);
    List<Exhibition> getExhibitionsByOrganizer(User organizer);
    Exhibition getExhibitionById(Long exhibitionId);
}