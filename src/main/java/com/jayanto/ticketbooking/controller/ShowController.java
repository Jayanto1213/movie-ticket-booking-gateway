package com.jayanto.ticketbooking.controller;
import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController @RequestMapping("/api/shows")
public class ShowController {
    private final ShowRepository shows; private final MovieRepository movies; private final TheatreRepository theatres;
    public ShowController(ShowRepository shows,MovieRepository movies,TheatreRepository theatres){this.shows=shows;this.movies=movies;this.theatres=theatres;}
    @GetMapping public List<Show> all(@RequestParam(required=false) Long movieId,@RequestParam(required=false) String city){
        if(movieId!=null)return shows.findByMovieId(movieId); if(city!=null)return shows.findByTheatreCityIgnoreCase(city); return shows.findAll();
    }
    @PostMapping public Show create(@Valid @RequestBody CreateShowRequest request){
        Movie movie=movies.findById(request.movieId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Movie not found"));
        Theatre theatre=theatres.findById(request.theatreId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Theatre not found"));
        return shows.save(new Show(movie,theatre,request.startTime(),request.ticketPrice(),request.totalSeats()));
    }
    public record CreateShowRequest(Long movieId,Long theatreId,java.time.LocalDateTime startTime,double ticketPrice,int totalSeats){}
}
