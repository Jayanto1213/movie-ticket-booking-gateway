package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name="movie_shows")
public class Show {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Movie movie;
    @ManyToOne(optional=false) private Theatre theatre;
    @NotNull private LocalDateTime startTime;
    @Positive private double ticketPrice;
    @Min(1) private int totalSeats;
    public Show() {}
    public Show(Movie movie,Theatre theatre,LocalDateTime startTime,double ticketPrice,int totalSeats){
        this.movie=movie;this.theatre=theatre;this.startTime=startTime;this.ticketPrice=ticketPrice;this.totalSeats=totalSeats;
    }
    public Long getId(){return id;} public Movie getMovie(){return movie;} public void setMovie(Movie v){movie=v;}
    public Theatre getTheatre(){return theatre;} public void setTheatre(Theatre v){theatre=v;}
    public LocalDateTime getStartTime(){return startTime;} public void setStartTime(LocalDateTime v){startTime=v;}
    public double getTicketPrice(){return ticketPrice;} public void setTicketPrice(double v){ticketPrice=v;}
    public int getTotalSeats(){return totalSeats;} public void setTotalSeats(int v){totalSeats=v;}
}
