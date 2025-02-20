package com.example.exbackend.controller;

import com.example.exbackend.model.Ticket;
import com.example.exbackend.model.User;
import com.example.exbackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/issue")
    public ResponseEntity<Ticket> issueTicket() {
        User user = getCurrentUser();
        Ticket ticket = ticketService.issueTicket(user);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/my-tickets")
    public ResponseEntity<List<Ticket>> getMyTickets() {
        User user = getCurrentUser();
        List<Ticket> myTickets = ticketService.getTicketsByUser(user);
        return ResponseEntity.ok(myTickets);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}