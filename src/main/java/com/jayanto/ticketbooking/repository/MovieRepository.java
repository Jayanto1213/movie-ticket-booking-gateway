package com.jayanto.ticketbooking.repository;
import com.jayanto.ticketbooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MovieRepository extends JpaRepository<Movie,Long> {}
