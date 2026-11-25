
package com.jayanto.ticketbooking.repository;

import com.jayanto.ticketbooking.model.SeatLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatLockRepository
        extends JpaRepository<SeatLock, Long> {

    Optional<SeatLock> findByShow_IdAndSeatNumber(
            Long showId,
            String seatNumber
    );

    List<SeatLock> findByShow_Id(Long showId);

    List<SeatLock> findByExpiresAtBefore(
            LocalDateTime currentTime
    );

    void deleteByExpiresAtBefore(
            LocalDateTime currentTime
    );

    void deleteByCustomerEmailIgnoreCase(
            String customerEmail
    );
}
