package main.java.com.example.trainapp.model;

import javax.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromLocation;
    private String toLocation;
    private Double price;
    @OneToOne
    private User user;
    @OneToOne
    private Seat seat;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFromLocation() {
        return fromLocation;
    }
    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }
    public String getToLocation() {
        return toLocation;
    }
    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Seat getSeat() {
        return seat;
    }
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    // Getters and setters
}
