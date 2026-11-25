
package com.jayanto.ticketbooking.service;

import com.jayanto.ticketbooking.model.*;
import com.jayanto.ticketbooking.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository payments;
    private final BookingRepository bookings;

    public PaymentService(
            PaymentRepository payments,
            BookingRepository bookings) {
        this.payments = payments;
        this.bookings = bookings;
    }

    @Transactional
    public Payment initiatePayment(Long bookingId) {

        Booking booking = bookings.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking is not waiting for payment");
        }

        if (booking.getExpiresAt() == null ||
                !booking.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking has expired");
        }

        if (payments.findByBooking_Id(bookingId).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Payment already initiated for this booking");
        }

        Payment payment = new Payment(
                booking,
                booking.getTotalAmount()
        );

        return payments.save(payment);
    }

    @Transactional
    public Payment completePayment(Long paymentId, boolean success) {

        Payment payment = payments.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Payment has already been processed");
        }

        Booking booking = payment.getBooking();

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Booking is no longer waiting for payment");
        }

        if (booking.getExpiresAt() == null ||
                !booking.getExpiresAt().isAfter(LocalDateTime.now())) {

            payment.setStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.CANCELLED);
            booking.getSeatBookings().clear();

            return payment;
        }

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            booking.getSeatBookings().clear();
            booking.setStatus(BookingStatus.CANCELLED);
        }

        return payment;
    }

    public Payment getPaymentByBookingId(Long bookingId) {

        return payments.findByBooking_Id(bookingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Payment not found for this booking"));
    }
}
