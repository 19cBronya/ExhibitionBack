package com.example.exbackend.controller;

import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;
import com.example.exbackend.service.ExhibitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exhibitions")
public class ExhibitionController {

    @Autowired
    private ExhibitionService exhibitionService;

    @PostMapping
    public ResponseEntity<Exhibition> createExhibition(@RequestBody Exhibition exhibition) {
        User organizer = getCurrentUser();
        Exhibition createdExhibition = exhibitionService.createExhibition(exhibition, organizer);
        return ResponseEntity.ok(createdExhibition);
    }

    @GetMapping("/my-exhibitions")
    public ResponseEntity<List<Exhibition>> getMyExhibitions() {
        User organizer = getCurrentUser();
        List<Exhibition> exhibitions = exhibitionService.getExhibitionsByOrganizer(organizer);
        return ResponseEntity.ok(exhibitions);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}