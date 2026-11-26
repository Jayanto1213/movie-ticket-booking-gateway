
package com.jayanto.ticketbooking.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateBookingRequest(

        @NotNull(message = "Show ID is required")
        @Positive(message = "Show ID must be greater than zero")
        Long showId,

        @NotBlank(message = "Customer name is required")
        @Size(min = 2, max = 50,
                message = "Customer name must be between 2 and 50 characters")
        String customerName,

        @NotBlank(message = "Customer email is required")
        @Email(message = "Please provide a valid email address")
        String customerEmail,

        @NotEmpty(message = "At least one seat must be selected")
        List<
                @NotBlank(message = "Seat number cannot be empty")
                @Pattern(regexp = "[A-Fa-f](10|[1-9])",
                        message = "Seat number must be between A1 and F10")
                        String
                > seatNumbers

) {}
