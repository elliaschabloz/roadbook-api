package dev.roadbook.api.trip;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(Long id) {
        super("Trip %d not found".formatted(id));
    }
}