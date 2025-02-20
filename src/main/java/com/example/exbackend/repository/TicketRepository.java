package com.example.exbackend.repository;

import com.example.exbackend.model.Ticket;
import com.example.exbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
}