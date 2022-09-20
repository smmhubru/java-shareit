package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELLED;

    public static Optional<BookingStatus> from(String stringStatus) {
        for (BookingStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
