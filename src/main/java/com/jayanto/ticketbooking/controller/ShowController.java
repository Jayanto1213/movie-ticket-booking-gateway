
package com.jayanto.ticketbooking.controller;

import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowRepository shows;
    private final MovieRepository movies;
    private final TheatreRepository theatres;
    private final SeatBookingRepository seatBookings;

    public ShowController(
            ShowRepository shows,
            MovieRepository movies,
            TheatreRepository theatres,
            SeatBookingRepository seatBookings) {
        this.shows = shows;
        this.movies = movies;
        this.theatres = theatres;
        this.seatBookings = seatBookings;
    }

    @GetMapping
    public List<Show> all(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String city) {

        if (movieId != null) {
            return shows.findByMovieId(movieId);
        }

        if (city != null) {
            return shows.findByTheatreCityIgnoreCase(city);
        }

        return shows.findAll();
    }

    @PostMapping
    public Show create(@Valid @RequestBody CreateShowRequest request) {

        Movie movie = movies.findById(request.movieId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie not found"));

        Theatre theatre = theatres.findById(request.theatreId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Theatre not found"));

        return shows.save(new Show(
                movie,
                theatre,
                request.startTime(),
                request.ticketPrice(),
                request.totalSeats()
        ));
    }

    @GetMapping("/{showId}/seats")
    public SeatAvailability getSeatAvailability(
            @PathVariable Long showId) {

        Show show = shows.findById(showId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Show not found"));

        Comparator<String> seatComparator = Comparator
                .comparingInt((String seat) -> seat.charAt(0))
                .thenComparingInt(seat ->
                        Integer.parseInt(seat.substring(1)));

        Set<String> bookedSeats = seatBookings.findByShow_Id(showId)
                .stream()
                .map(SeatBooking::getSeatNumber)
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(seatComparator)));

        Set<String> availableSeats = new TreeSet<>(seatComparator);

        for (int i = 0; i < show.getTotalSeats(); i++) {

            char row = (char) ('A' + (i / 10));
            int seatNumber = (i % 10) + 1;

            String seat = row + String.valueOf(seatNumber);

            if (!bookedSeats.contains(seat)) {
                availableSeats.add(seat);
            }
        }

        return new SeatAvailability(
                showId,
                show.getTotalSeats(),
                bookedSeats,
                availableSeats
        );
    }

    public record SeatAvailability(
            Long showId,
            int totalSeats,
            Set<String> bookedSeats,
            Set<String> availableSeats
    ) {
    }

    public record CreateShowRequest(
            Long movieId,
            Long theatreId,
            java.time.LocalDateTime startTime,
            double ticketPrice,
            int totalSeats
    ) {
    }
}
