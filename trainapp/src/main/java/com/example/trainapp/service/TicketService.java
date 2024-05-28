package main.java.com.example.trainapp.service;

import com.example.trainapp.dto.TicketRequest;
import com.example.trainapp.dto.SeatRequest;
import com.example.trainapp.exception.ResourceNotFoundException;
import com.example.trainapp.model.Seat;
import com.example.trainapp.model.Ticket;
import com.example.trainapp.model.User;
import com.example.trainapp.repository.SeatRepository;
import com.example.trainapp.repository.TicketRepository;
import com.example.trainapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new ticket (purchase)
    public Ticket purchaseTicket(TicketRequest ticketRequest) {
        // Create new User entity from request
        User user = new User();
        user.setFirstName(ticketRequest.getFirstName());
        user.setLastName(ticketRequest.getLastName());
        user.setEmail(ticketRequest.getEmail());

        // Save user to repository
        User savedUser = userRepository.save(user);

        // Find a seat 
        Seat seat = allocateSeat();

        // Create new Ticket entity
        Ticket ticket = new Ticket();
        ticket.setFromLocation(ticketRequest.getFromLocation());
        ticket.setToLocation(ticketRequest.getToLocation());
        ticket.setPrice(20.0); // Fixed price as per requirement
        ticket.setUser(savedUser);
        ticket.setSeat(seat);

        // Save ticket to repository
        return ticketRepository.save(ticket);
    }

    // Get ticket by ID
    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id " + ticketId));
    }

    // Get all tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Modify seat of an existing ticket
    public Ticket modifySeat(Long ticketId, SeatRequest seatRequest) {
        // Find ticket by ID
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id " + ticketId));

        // Find current seat allocated to the user
        Seat currentSeat = ticket.getSeat();

        // Find new seat by requested section and seat number
        Seat newSeat = seatRepository.findBySectionAndSeatNumber(seatRequest.getSection(), seatRequest.getSeatNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with section " +
                        seatRequest.getSection() + " and seat number " + seatRequest.getSeatNumber()));

        // Ensure the new seat is not already occupied
        if (newSeat.isOccupied()) {
            throw new ResourceNotFoundException("Seat " + seatRequest.getSection() + "-" + seatRequest.getSeatNumber() + " is already occupied.");
        }

        // Mark the current seat as not occupied
        currentSeat.setOccupied(false);
        seatRepository.save(currentSeat);

        // Mark the new seat as occupied
        newSeat.setOccupied(true);
        seatRepository.save(newSeat);

        // Update ticket with new seat
        ticket.setSeat(newSeat);

        // Save updated ticket to repository
        return ticketRepository.save(ticket);
    }

    // Remove a user from the train (delete a ticket)
    public void removeTicket(Long ticketId) {
        // Find ticket by ID
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id " + ticketId));

        // Set the current seat as not occupied
        Seat seat = ticket.getSeat();
        seat.setOccupied(false);
        seatRepository.save(seat);

        // Delete ticket from repository
        ticketRepository.delete(ticket);
    }

    // Allocate seat 
    private Seat allocateSeat() {
        // Find an available seat 
        Optional<Seat> availableSeat = seatRepository.findByIsOccupiedFalse();
        if (!availableSeat.isPresent()) {
            throw new ResourceNotFoundException("No available seats found");
        }

        // Mark the seat as occupied and save it
        Seat seat = availableSeat.get();
        seat.setOccupied(true);
        seatRepository.save(seat);

        return seat;
    }
}
