
package com.jayanto.ticketbooking.service;

import com.jayanto.ticketbooking.repository.SeatLockRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class SeatLockCleanupScheduler {

    private final SeatLockRepository seatLocks;

    public SeatLockCleanupScheduler(
            SeatLockRepository seatLocks) {
        this.seatLocks = seatLocks;
    }

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void removeExpiredLocks() {

        seatLocks.deleteByExpiresAtBefore(
                LocalDateTime.now()
        );

        System.out.println("Expired seat locks cleaned up.");
    }
}
