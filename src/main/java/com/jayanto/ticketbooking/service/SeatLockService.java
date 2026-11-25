
package com.jayanto.ticketbooking.service;

import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatLockService {

    private final SeatLockRepository seatLocks;
    private final ShowRepository shows;
    private final SeatBookingRepository seatBookings;

    public SeatLockService(
            SeatLockRepository seatLocks,
            ShowRepository shows,
            SeatBookingRepository seatBookings) {

        this.seatLocks = seatLocks;
        this.shows = shows;
        this.seatBookings = seatBookings;
    }

    @Transactional
    public SeatLock lockSeat(
            Long showId,
            String seatNumber,
            String customerEmail) {

        Show show = shows.findById(showId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Show not found"
                ));

        String seat = seatNumber.trim().toUpperCase();

        if (!seat.matches("[A-F](10|[1-9])")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid seat: " + seat
            );
        }

        if (seatBookings.existsByShow_IdAndSeatNumber(showId, seat)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Seat already booked: " + seat
            );
        }

        LocalDateTime now = LocalDateTime.now();

        seatLocks.deleteByExpiresAtBefore(now);

        var existingLock = seatLocks
                .findByShow_IdAndSeatNumber(showId, seat);

        if (existingLock.isPresent()) {

            SeatLock lock = existingLock.get();

            if (lock.getExpiresAt().isAfter(now)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Seat already locked by another user"
                );
            }

            seatLocks.delete(lock);
            seatLocks.flush();
        }

        SeatLock newLock = new SeatLock(
                show,
                seat,
                customerEmail,
                now,
                now.plusMinutes(5)
        );

        return seatLocks.saveAndFlush(newLock);
    }

    public List<SeatLock> getLocks(Long showId) {

        if (!shows.existsById(showId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Show not found"
            );
        }

        seatLocks.deleteByExpiresAtBefore(LocalDateTime.now());

        return seatLocks.findByShow_Id(showId);
    }
}
