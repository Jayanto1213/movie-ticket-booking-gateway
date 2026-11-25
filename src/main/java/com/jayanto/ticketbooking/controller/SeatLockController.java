
package com.jayanto.ticketbooking.controller;

import com.jayanto.ticketbooking.model.SeatLock;
import com.jayanto.ticketbooking.service.SeatLockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-locks")
public class SeatLockController {

    private final SeatLockService seatLockService;

    public SeatLockController(SeatLockService seatLockService) {
        this.seatLockService = seatLockService;
    }

    @PostMapping
    public SeatLock lockSeat(@RequestBody CreateSeatLockRequest request) {

        return seatLockService.lockSeat(
                request.showId(),
                request.seatNumber(),
                request.customerEmail()
        );
    }

    @GetMapping
    public List<SeatLock> getLocks(@RequestParam Long showId) {

        return seatLockService.getLocks(showId);
    }

    public record CreateSeatLockRequest(
            Long showId,
            String seatNumber,
            String customerEmail
    ) {
    }
}
