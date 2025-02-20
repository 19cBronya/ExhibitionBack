package com.example.exbackend.service.impl;

import com.example.exbackend.exception.ResourceNotFoundException;
import com.example.exbackend.model.RegistrationRequest;
import com.example.exbackend.model.User;
import com.example.exbackend.model.UserRole;
import com.example.exbackend.repository.RegistrationRequestRepository;
import com.example.exbackend.repository.UserRepository;
import com.example.exbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRequestRepository registrationRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setContactInfo(request.getContactInfo());
        user.setRole(request.getRole());
        user.setApproved(false);

        userRepository.save(user);
        request.setUser(user);
        registrationRequestRepository.save(request);
        return user;
    }

    @Override
    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return user;
    }

    @Override
    public void approveRegistration(Long requestId) {
        RegistrationRequest request = registrationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration request not found"));
        User user = request.getUser();
        user.setApproved(true);
        userRepository.save(user);
        registrationRequestRepository.delete(request);
    }

    @Override
    public void rejectRegistration(Long requestId) {
        RegistrationRequest request = registrationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration request not found"));
        userRepository.delete(request.getUser());
        registrationRequestRepository.delete(request);
    }

    @Override
    public List<RegistrationRequest> getPendingRegistrations() {
        return registrationRequestRepository.findAllByApprovedFalse();
    }

    @Override
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findAllByRole(role);
    }
}