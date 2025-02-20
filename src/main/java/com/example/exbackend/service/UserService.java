package com.example.exbackend.service;

import com.example.exbackend.model.RegistrationRequest;
import com.example.exbackend.model.User;
import com.example.exbackend.model.UserRole;

import java.util.List;

public interface UserService {
    User registerUser(RegistrationRequest request);
    User loginUser(String username, String password);
    void approveRegistration(Long requestId);
    void rejectRegistration(Long requestId);
    List<RegistrationRequest> getPendingRegistrations();
    List<User> getUsersByRole(UserRole role);
}