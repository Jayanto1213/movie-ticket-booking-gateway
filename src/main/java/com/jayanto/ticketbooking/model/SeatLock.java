
package com.jayanto.ticketbooking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "seat_locks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_locked_seat",
                        columnNames = {"show_id", "seat_number"}
                )
        }
)
public class SeatLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private LocalDateTime lockedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public SeatLock() {
    }

    public SeatLock(
            Show show,
            String seatNumber,
            String customerEmail,
            LocalDateTime lockedAt,
            LocalDateTime expiresAt) {

        this.show = show;
        this.seatNumber = seatNumber;
        this.customerEmail = customerEmail;
        this.lockedAt = lockedAt;
        this.expiresAt = expiresAt;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
