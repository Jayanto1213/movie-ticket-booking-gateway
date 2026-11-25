
package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Show show;

    private String customerName;
    private String customerEmail;
    private String seatNumbers;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime bookedAt;

    @OneToMany(mappedBy = "booking",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<SeatBooking> seatBookings = new ArrayList<>();

    public Booking() {
    }

    public Booking(Show show, String customerName,
                   String customerEmail, String seatNumbers,
                   double totalAmount) {

        this.show = show;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.seatNumbers = seatNumbers;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING_PAYMENT;
        this.bookedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Show getShow() {
        return show;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public List<SeatBooking> getSeatBookings() {
        return seatBookings;
    }

    public void addSeatBooking(SeatBooking seatBooking) {
        seatBookings.add(seatBooking);
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
