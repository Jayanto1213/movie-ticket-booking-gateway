
package com.jayanto.ticketbooking.service;

import com.jayanto.ticketbooking.dto.CreateBookingRequest;
import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingService {

    private final BookingRepository bookings;
    private final ShowRepository shows;
    private final SeatBookingRepository seatBookings;
    private final SeatLockRepository seatLocks;
    private final PaymentRepository payments;

    public BookingService(
            BookingRepository bookings,
            ShowRepository shows,
            SeatBookingRepository seatBookings,
            SeatLockRepository seatLocks,
            PaymentRepository payments) {

        this.bookings = bookings;
        this.shows = shows;
        this.seatBookings = seatBookings;
        this.seatLocks = seatLocks;
        this.payments = payments;
    }

    @Transactional
    public Booking create(CreateBookingRequest request) {

        Show show = shows.findById(request.showId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Show not found"));

        List<String> seats = request.seatNumbers()
                .stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .distinct()
                .toList();

        if (seats.isEmpty() ||
                seats.size() != request.seatNumbers().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid or duplicate seat numbers");
        }

        String customerEmail = request.customerEmail().trim();
        LocalDateTime now = LocalDateTime.now();

        seatLocks.deleteByExpiresAtBefore(now);

        List<SeatLock> locksToRemove = new ArrayList<>();

        for (String seat : seats) {

            if (!seat.matches("[A-F](10|[1-9])")) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid seat: " + seat);
            }

            if (seatBookings.existsByShow_IdAndSeatNumber(
                    show.getId(), seat)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Seat already booked: " + seat);
            }

            Optional<SeatLock> existingLock =
                    seatLocks.findByShow_IdAndSeatNumber(
                            show.getId(), seat);

            if (existingLock.isPresent()) {

                SeatLock lock = existingLock.get();

                if (!lock.getCustomerEmail()
                        .equalsIgnoreCase(customerEmail)) {

                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Seat is temporarily locked by another customer: "
                                    + seat);
                }

                locksToRemove.add(lock);
            }
        }

        double amount = show.getTicketPrice() * seats.size();

        Booking booking = new Booking(
                show,
                request.customerName(),
                customerEmail,
                String.join(",", seats),
                amount
        );

        for (String seat : seats) {
            booking.addSeatBooking(
                    new SeatBooking(show, seat, booking));
        }

        try {

            Booking savedBooking = bookings.saveAndFlush(booking);

            seatLocks.deleteAll(locksToRemove);
            seatLocks.flush();

            return savedBooking;

        } catch (DataIntegrityViolationException exception) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "One or more seats have already been booked");
        }
    }

    public Booking get(Long id) {

        return bookings.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Booking not found"));
    }

    public List<Booking> byEmail(String email) {

        return bookings.findByCustomerEmailIgnoreCase(email);
    }

    @Transactional
    public Booking cancel(Long id) {

        Booking booking = get(id);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking already cancelled");
        }

        payments.findByBooking_Id(booking.getId())
                .ifPresent(payment -> {
                    if (payment.getStatus() == PaymentStatus.PENDING) {
                        payment.setStatus(PaymentStatus.FAILED);
                    }
                });

        booking.getSeatBookings().clear();
        booking.setStatus(BookingStatus.CANCELLED);

        return booking;
    }
}
