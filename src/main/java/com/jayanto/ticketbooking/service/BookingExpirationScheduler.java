
package com.jayanto.ticketbooking.service;

import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.BookingRepository;
import com.jayanto.ticketbooking.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingExpirationScheduler {

    private final BookingRepository bookings;
    private final PaymentRepository payments;

    public BookingExpirationScheduler(
            BookingRepository bookings,
            PaymentRepository payments) {
        this.bookings = bookings;
        this.payments = payments;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void expireBookings() {

        List<Booking> expiredBookings =
                bookings.findByStatusAndExpiresAtBefore(
                        BookingStatus.PENDING_PAYMENT,
                        LocalDateTime.now()
                );

        for (Booking booking : expiredBookings) {

            booking.getSeatBookings().clear();

            booking.setStatus(BookingStatus.CANCELLED);

            payments.findByBooking_Id(booking.getId())
                    .ifPresent(payment -> {
                        if (payment.getStatus() == PaymentStatus.PENDING) {
                            payment.setStatus(PaymentStatus.FAILED);
                        }
                    });
        }
    }
}
