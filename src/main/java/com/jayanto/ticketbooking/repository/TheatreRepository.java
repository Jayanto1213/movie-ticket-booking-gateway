package com.jayanto.ticketbooking.repository;
import com.jayanto.ticketbooking.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TheatreRepository extends JpaRepository<Theatre,Long> {
    List<Theatre> findByCityIgnoreCase(String city);
}
