package com.example.exbackend.service.impl;

import com.example.exbackend.model.Ticket;
import com.example.exbackend.model.User;
import com.example.exbackend.repository.TicketRepository;
import com.example.exbackend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket issueTicket(User user) {
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findByUser(user);
    }
}