
package com.jayanto.ticketbooking.repository;

import com.jayanto.ticketbooking.model.SeatBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatBookingRepository extends JpaRepository<SeatBooking, Long> {

    boolean existsByShow_IdAndSeatNumber(Long showId, String seatNumber);

    List<SeatBooking> findByShow_Id(Long showId);

    long deleteByBooking_Id(Long bookingId);
}
