package com.jayanto.ticketbooking.repository;
import com.jayanto.ticketbooking.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findByMovieId(Long movieId);
    List<Show> findByTheatreCityIgnoreCase(String city);
}
