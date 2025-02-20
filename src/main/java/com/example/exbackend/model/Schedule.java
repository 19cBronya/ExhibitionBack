package com.example.exbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int halfDay; // Half day number (e.g., 1 for morning, 2 for afternoon)
    private String activityType; // Activity type (e.g., schedule, keynote, workshop)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
}