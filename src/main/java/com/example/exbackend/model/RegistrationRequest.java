package com.example.exbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String contactInfo;
    private UserRole role;
    private boolean approved;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}