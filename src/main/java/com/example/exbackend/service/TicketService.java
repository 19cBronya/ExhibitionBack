package com.example.exbackend.service;

import com.example.exbackend.model.Ticket;
import com.example.exbackend.model.User;

import java.util.List;

public interface TicketService {
    Ticket issueTicket(User user);
    List<Ticket> getTicketsByUser(User user);
}