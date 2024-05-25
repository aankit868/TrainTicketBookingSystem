package test.java.com.example.trainapp.service;

import com.example.trainapp.dto.TicketRequest;
import com.example.trainapp.dto.SeatRequest;
import com.example.trainapp.model.Seat;
import com.example.trainapp.model.Ticket;
import com.example.trainapp.model.User;
import com.example.trainapp.repository.SeatRepository;
import com.example.trainapp.repository.TicketRepository;
import com.example.trainapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPurchaseTicket() {
        // Prepare mock data
        TicketRequest request = new TicketRequest();
        request.setFromLocation("London");
        request.setToLocation("France");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");

        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSection("A");
        seat.setSeatNumber("A1");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setFromLocation("London");
        ticket.setToLocation("France");
        ticket.setPrice(20.0);
        ticket.setUser(user);
        ticket.setSeat(seat);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(seat));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Test purchaseTicket method
        Ticket result = ticketService.purchaseTicket(request);

        assertNotNull(result);
        assertEquals("London", result.getFromLocation());
        assertEquals("France", result.getToLocation());
        assertEquals(20.0, result.getPrice());
        assertEquals("John", result.getUser().getFirstName());
        assertEquals("Doe", result.getUser().getLastName());
        assertEquals("A1", result.getSeat().getSeatNumber());

        verify(userRepository, times(1)).save(any(User.class));
        verify(seatRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testGetTicket() {
        // Prepare mock data
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        // Test getTicket method
        Ticket result = ticketService.getTicket(ticketId);

        assertNotNull(result);
        assertEquals(ticketId, result.getId());

        verify(ticketRepository, times(1)).findById(ticketId);
    }

    @Test
    void testModifySeat() {
        // Prepare mock data
        Long ticketId = 1L;
        SeatRequest seatRequest = new SeatRequest();
        seatRequest.setSection("B");
        seatRequest.setSeatNumber("B1");

        Seat seat = new Seat();
        seat.setId(2L);
        seat.setSection("B");
        seat.setSeatNumber("B1");

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setSeat(seat);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(seat));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Test modifySeat method
        Ticket result = ticketService.modifySeat(ticketId, seatRequest);

        assertNotNull(result);
        assertEquals("B", result.getSeat().getSection());
        assertEquals("B1", result.getSeat().getSeatNumber());

        verify(ticketRepository, times(1)).findById(ticketId);
        verify(seatRepository, times(1)).findById(anyLong());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
}
