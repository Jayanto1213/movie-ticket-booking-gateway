package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Show show;
    private String customerName;
    private String customerEmail;
    private String seatNumbers;
    private double totalAmount;
    @Enumerated(EnumType.STRING) private BookingStatus status;
    private LocalDateTime bookedAt;
    public Booking() {}
    public Booking(Show show,String customerName,String customerEmail,String seatNumbers,double totalAmount){
        this.show=show;this.customerName=customerName;this.customerEmail=customerEmail;this.seatNumbers=seatNumbers;
        this.totalAmount=totalAmount;this.status=BookingStatus.CONFIRMED;this.bookedAt=LocalDateTime.now();
    }
    public Long getId(){return id;} public Show getShow(){return show;} public String getCustomerName(){return customerName;}
    public String getCustomerEmail(){return customerEmail;} public String getSeatNumbers(){return seatNumbers;}
    public double getTotalAmount(){return totalAmount;} public BookingStatus getStatus(){return status;}
    public LocalDateTime getBookedAt(){return bookedAt;} public void setStatus(BookingStatus v){status=v;}
}
