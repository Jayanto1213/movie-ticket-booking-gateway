package com.jayanto.ticketbooking.controller;
import com.jayanto.ticketbooking.model.Movie;
import com.jayanto.ticketbooking.repository.MovieRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository repo;
    public MovieController(MovieRepository repo){this.repo=repo;}
    @GetMapping public List<Movie> all(){return repo.findAll();}
    @GetMapping("/{id}") public Movie one(@PathVariable Long id){return repo.findById(id).orElseThrow();}
    @PostMapping public Movie create(@Valid @RequestBody Movie movie){return repo.save(movie);}
    @PutMapping("/{id}") public Movie update(@PathVariable Long id,@Valid @RequestBody Movie data){
        Movie m=repo.findById(id).orElseThrow();m.setTitle(data.getTitle());m.setGenre(data.getGenre());
        m.setDurationMinutes(data.getDurationMinutes());m.setLanguage(data.getLanguage());m.setDescription(data.getDescription());m.setPosterUrl(data.getPosterUrl());return repo.save(m);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id){repo.deleteById(id);}
}
