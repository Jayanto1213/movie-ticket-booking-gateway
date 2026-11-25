package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String title;
    @NotBlank private String genre;
    @Min(1) private int durationMinutes;
    @NotBlank private String language;
    private String description;
    private String posterUrl;

    public Movie() {}
    public Movie(String title, String genre, int durationMinutes, String language, String description, String posterUrl) {
        this.title=title; this.genre=genre; this.durationMinutes=durationMinutes; this.language=language; this.description=description; this.posterUrl=posterUrl;
    }
    public Long getId(){return id;} public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getGenre(){return genre;} public void setGenre(String v){genre=v;}
    public int getDurationMinutes(){return durationMinutes;} public void setDurationMinutes(int v){durationMinutes=v;}
    public String getLanguage(){return language;} public void setLanguage(String v){language=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public String getPosterUrl(){return posterUrl;} public void setPosterUrl(String v){posterUrl=v;}
}
