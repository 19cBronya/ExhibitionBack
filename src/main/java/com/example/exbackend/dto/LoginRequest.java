package com.example.exbackend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}