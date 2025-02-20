package com.example.exbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String contactInfo;
    private UserRole role;
    private boolean approved;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Exhibition> exhibitionsOrganized;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

    @OneToMany(mappedBy = "exhibitor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Booth> boothsAssigned;

    @OneToMany(mappedBy = "exhibitor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Product> productsUploaded;
}