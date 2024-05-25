package test.java.com.example.trainapp.controller;

import com.example.trainapp.dto.TicketRequest;
import com.example.trainapp.dto.SeatRequest;
import com.example.trainapp.model.Ticket;
import com.example.trainapp.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TicketControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    void testPurchaseTicket() throws Exception {
        // Prepare mock data
        TicketRequest request = new TicketRequest();
        request.setFromLocation("London");
        request.setToLocation("France");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setFromLocation("London");
        ticket.setToLocation("France");
        ticket.setPrice(20.0);

        when(ticketService.purchaseTicket(any(TicketRequest.class))).thenReturn(ticket);

        // Test /api/tickets endpoint
        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fromLocation\": \"London\", \"toLocation\": \"France\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromLocation").value("London"))
                .andExpect(jsonPath("$.toLocation").value("France"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    void testGetTicket() throws Exception {
        // Prepare mock data
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketService.getTicket(ticketId)).thenReturn(ticket);

        // Test /api/tickets/{id} endpoint
        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId));
    }

    @Test
    void testModifySeat() throws Exception {
        // Prepare mock data
        Long ticketId = 1L;
        SeatRequest seatRequest = new SeatRequest();
        seatRequest.setSection("B");
        seatRequest.setSeatNumber("B1");

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketService.modifySeat(ticketId, seatRequest)).thenReturn(ticket);

        // Test /api/tickets/{id}/seat endpoint
        mockMvc.perform(put("/api/tickets/{id}/seat", ticketId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"section\": \"B\", \"seatNumber\": \"B1\"}"))
                .andExpect(status().isOk());
    }
}
