package com.example.exbackend.service.impl;

import com.example.exbackend.exception.ResourceNotFoundException;
import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;
import com.example.exbackend.repository.ExhibitionRepository;
import com.example.exbackend.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Override
    public Exhibition createExhibition(Exhibition exhibition, User organizer) {
        exhibition.setOrganizer(organizer);
        return exhibitionRepository.save(exhibition);
    }

    @Override
    public List<Exhibition> getExhibitionsByOrganizer(User organizer) {
        return exhibitionRepository.findByOrganizer(organizer);
    }

    @Override
    public Exhibition getExhibitionById(Long exhibitionId) {
        Optional<Exhibition> exhibitionOptional = exhibitionRepository.findById(exhibitionId);
        return exhibitionOptional.orElseThrow(() -> new ResourceNotFoundException("Exhibition not found with id " + exhibitionId));
    }

}