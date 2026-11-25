package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Theatre {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String name;
    @NotBlank private String city;
    private String address;
    public Theatre() {}
    public Theatre(String name,String city,String address){this.name=name;this.city=city;this.address=address;}
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
}
