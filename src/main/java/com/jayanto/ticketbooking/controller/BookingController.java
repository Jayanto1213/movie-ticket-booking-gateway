package com.jayanto.ticketbooking.controller;
import com.jayanto.ticketbooking.dto.CreateBookingRequest;
import com.jayanto.ticketbooking.model.Booking;
import com.jayanto.ticketbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService service;
    public BookingController(BookingService service){this.service=service;}
    @PostMapping public Booking create(@Valid @RequestBody CreateBookingRequest request){return service.create(request);}
    @GetMapping("/{id}") public Booking one(@PathVariable Long id){return service.get(id);}
    @GetMapping public List<Booking> byEmail(@RequestParam String email){return service.byEmail(email);}
    @PatchMapping("/{id}/cancel") public Booking cancel(@PathVariable Long id){return service.cancel(id);}
}
