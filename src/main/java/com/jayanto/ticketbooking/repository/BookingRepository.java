package com.jayanto.ticketbooking.repository;
import com.jayanto.ticketbooking.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByCustomerEmailIgnoreCase(String email);
    boolean existsByShowIdAndStatusAndSeatNumbersContaining(Long showId, BookingStatus status, String seatNumber);
}
