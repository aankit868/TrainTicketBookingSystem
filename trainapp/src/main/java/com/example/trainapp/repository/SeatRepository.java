package main.java.com.example.trainapp.repository;

import com.example.trainapp.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySectionAndSeatNumber(String section, String seatNumber);
    Optional<Seat> findByIsOccupiedFalse();
}
