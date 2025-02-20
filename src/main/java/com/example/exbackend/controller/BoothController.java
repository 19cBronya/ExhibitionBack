package com.example.exbackend.controller;

import com.example.exbackend.model.Booth;
import com.example.exbackend.model.Exhibition;
import com.example.exbackend.model.User;
import com.example.exbackend.service.BoothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booths")
public class BoothController {

    @Autowired
    private BoothService boothService;

    @PostMapping("/assign")
    public ResponseEntity<Booth> assignBoothToExhibitor(@RequestBody Booth booth, @RequestParam Long exhibitionId) {
        User exhibitor = getCurrentUser();
        Exhibition exhibition = getExhibitionById(exhibitionId); // Assume this is implemented
        Booth assignedBooth = boothService.assignBoothToExhibitor(booth, exhibition, exhibitor);
        return ResponseEntity.ok(assignedBooth);
    }

    @GetMapping("/available-for-exhibition/{exhibitionId}")
    public ResponseEntity<List<Booth>> getAvailableBoothsForExhibition(@PathVariable Long exhibitionId) {
        Exhibition exhibition = getExhibitionById(exhibitionId); // Assume this is implemented
        List<Booth> availableBooths = boothService.getAvailableBoothsForExhibition(exhibition);
        return ResponseEntity.ok(availableBooths);
    }

    @GetMapping("/my-booths")
    public ResponseEntity<List<Booth>> getMyBooths() {
        User exhibitor = getCurrentUser();
        List<Booth> myBooths = boothService.getBoothsByExhibitor(exhibitor);
        return ResponseEntity.ok(myBooths);
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