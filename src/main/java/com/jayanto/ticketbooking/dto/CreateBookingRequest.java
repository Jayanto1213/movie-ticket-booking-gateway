package com.jayanto.ticketbooking.dto;
import jakarta.validation.constraints.*;
import java.util.List;
public record CreateBookingRequest(
    @NotNull Long showId,
    @NotBlank String customerName,
    @Email @NotBlank String customerEmail,
    @NotEmpty List<@NotBlank String> seatNumbers
) {}
