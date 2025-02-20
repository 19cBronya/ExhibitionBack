package com.example.exbackend.dto;

import com.example.exbackend.model.UserRole;
import lombok.Data;

@Data
public class RegistrationRequestDto {
    private String username;
    private String password;
    private String contactInfo;
    private UserRole role;
}