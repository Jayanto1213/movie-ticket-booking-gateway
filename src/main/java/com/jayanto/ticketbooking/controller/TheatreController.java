package com.jayanto.ticketbooking.controller;
import com.jayanto.ticketbooking.model.Theatre;
import com.jayanto.ticketbooking.repository.TheatreRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/theatres")
public class TheatreController {
    private final TheatreRepository repo;
    public TheatreController(TheatreRepository repo){this.repo=repo;}
    @GetMapping public List<Theatre> all(@RequestParam(required=false) String city){return city==null?repo.findAll():repo.findByCityIgnoreCase(city);}
    @PostMapping public Theatre create(@Valid @RequestBody Theatre theatre){return repo.save(theatre);}
}
