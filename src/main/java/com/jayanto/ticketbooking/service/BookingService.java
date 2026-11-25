package com.jayanto.ticketbooking.service;

import com.jayanto.ticketbooking.dto.CreateBookingRequest;
import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class BookingService {
    private final BookingRepository bookings;
    private final ShowRepository shows;
    public BookingService(BookingRepository bookings, ShowRepository shows) { this.bookings=bookings; this.shows=shows; }

    @Transactional
    public Booking create(CreateBookingRequest request) {
        Show show=shows.findById(request.showId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Show not found"));
        List<String> seats=request.seatNumbers().stream().map(String::trim).map(String::toUpperCase).distinct().toList();
        if(seats.size()!=request.seatNumbers().size()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Duplicate seat numbers");
        for(String seat:seats) {
            if(!seat.matches("[A-Z]{1,2}[0-9]{1,2}")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid seat: "+seat);
            if(bookings.existsByShowIdAndStatusAndSeatNumbersContaining(show.getId(),BookingStatus.CONFIRMED,seat))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Seat already booked: "+seat);
        }
        double amount=show.getTicketPrice()*seats.size();
        return bookings.save(new Booking(show,request.customerName(),request.customerEmail(),String.join(",",seats),amount));
    }

    public Booking get(Long id) {
        return bookings.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking not found"));
    }
    public List<Booking> byEmail(String email){return bookings.findByCustomerEmailIgnoreCase(email);}
    @Transactional
    public Booking cancel(Long id) {
        Booking booking=get(id);
        if(booking.getStatus()==BookingStatus.CANCELLED) throw new ResponseStatusException(HttpStatus.CONFLICT,"Booking already cancelled");
        booking.setStatus(BookingStatus.CANCELLED);
        return booking;
    }
}
