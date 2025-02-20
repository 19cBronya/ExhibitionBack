package com.example.exbackend.controller;

import com.example.exbackend.dto.LoginRequest;
import com.example.exbackend.dto.RegistrationRequestDto;
import com.example.exbackend.model.RegistrationRequest;
import com.example.exbackend.model.User;
import com.example.exbackend.model.UserRole;
import com.example.exbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequestDto requestDto) {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername(requestDto.getUsername());
        request.setPassword(requestDto.getPassword());
        request.setContactInfo(requestDto.getContactInfo());
        request.setRole(requestDto.getRole());

        userService.registerUser(request);
        return ResponseEntity.ok("Registration request submitted successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginRequest loginRequest) {
        User user = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/pending-registrations")
    public ResponseEntity<List<RegistrationRequest>> getPendingRegistrations() {
        List<RegistrationRequest> pendingRegistrations = userService.getPendingRegistrations();
        return ResponseEntity.ok(pendingRegistrations);
    }

    @PutMapping("/approve-registration/{requestId}")
    public ResponseEntity<String> approveRegistration(@PathVariable Long requestId) {
        userService.approveRegistration(requestId);
        return ResponseEntity.ok("Registration approved successfully.");
    }

    @DeleteMapping("/reject-registration/{requestId}")
    public ResponseEntity<String> rejectRegistration(@PathVariable Long requestId) {
        userService.rejectRegistration(requestId);
        return ResponseEntity.ok("Registration rejected successfully.");
    }

    @GetMapping("/by-role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable UserRole role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}