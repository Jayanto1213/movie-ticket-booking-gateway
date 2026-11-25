
package com.jayanto.ticketbooking.controller;

import com.jayanto.ticketbooking.model.Payment;
import com.jayanto.ticketbooking.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate/{bookingId}")
    public Payment initiatePayment(@PathVariable Long bookingId) {
        return paymentService.initiatePayment(bookingId);
    }

    @PatchMapping("/{paymentId}/complete")
    public Payment completePayment(
            @PathVariable Long paymentId,
            @RequestParam boolean success) {

        return paymentService.completePayment(paymentId, success);
    }

    @GetMapping("/booking/{bookingId}")
    public Payment getPaymentByBookingId(
            @PathVariable Long bookingId) {

        return paymentService.getPaymentByBookingId(bookingId);
    }
}
