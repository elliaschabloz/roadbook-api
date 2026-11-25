package dev.roadbook.api.trip;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TripRequest(
        @NotBlank String name,
        String destination,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
    @AssertTrue(message = "endDate must be on or after startDate")
    public boolean isEndDateValid() {
        return startDate == null || endDate == null || !endDate.isBefore(startDate);
    }
}