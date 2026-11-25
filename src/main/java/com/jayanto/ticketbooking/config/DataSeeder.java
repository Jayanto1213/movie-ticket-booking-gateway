package com.jayanto.ticketbooking.config;
import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
@Configuration
public class DataSeeder {
    @Bean CommandLineRunner seed(MovieRepository movies,TheatreRepository theatres,ShowRepository shows){
        return args -> {
            if(movies.count()==0 && theatres.count()==0){
                Movie movie=movies.save(new Movie("The Last Journey","Adventure",142,"Hindi","A sample movie for testing the booking flow.",""));
                Theatre theatre=theatres.save(new Theatre("PVR Sakchi","Jamshedpur","Sakchi, Jamshedpur"));
                shows.save(new Show(movie,theatre,LocalDateTime.now().plusDays(1).withHour(18).withMinute(30).withSecond(0).withNano(0),220.0,60));
            }
        };
    }
}
