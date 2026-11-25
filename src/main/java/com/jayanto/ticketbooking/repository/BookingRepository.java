
package com.jayanto.ticketbooking.repository;

import com.jayanto.ticketbooking.model.Booking;
import com.jayanto.ticketbooking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerEmailIgnoreCase(String email);

    List<Booking> findByStatusAndExpiresAtBefore(
            BookingStatus status,
            LocalDateTime currentTime
    );
}
