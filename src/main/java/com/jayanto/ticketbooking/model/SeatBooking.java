
package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "seat_booking",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_seat",
                        columnNames = {"show_id", "seat_number"}
                )
        }
)
public class SeatBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public SeatBooking() {
    }

    public SeatBooking(Show show, String seatNumber, Booking booking) {
        this.show = show;
        this.seatNumber = seatNumber;
        this.booking = booking;
    }

    public Long getId() {
        return id;
    }

    public Show getShow() {
        return show;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public Booking getBooking() {
        return booking;
    }
}
