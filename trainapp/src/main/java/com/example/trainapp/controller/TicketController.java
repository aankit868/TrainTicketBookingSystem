package main.java.com.example.trainapp.controller;

import com.example.trainapp.dto.TicketRequest;
import com.example.trainapp.dto.SeatRequest;
import com.example.trainapp.model.Ticket;
import com.example.trainapp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody TicketRequest request) {
        return ticketService.purchaseTicket(request);
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }

    @GetMapping("/seats/{section}")
    public List<User> getUsersBySection(@PathVariable String section) {
        return ticketService.getUsersBySection(section);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id) {
        ticketService.removeUser(id);
    }

    @PutMapping("/{id}/seat")
    public Ticket modifySeat(@PathVariable Long id, @RequestBody SeatRequest seatRequest) {
        return ticketService.modifySeat(id, seatRequest);
    }
}
