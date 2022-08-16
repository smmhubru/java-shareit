package ru.practicum.shareit.booking;

import java.util.Optional;
import java.util.Set;

/**
 * Interface to interact with Booking Storage.
 */
public interface BookingStorage {
    Optional<Booking> addBooking(Booking booking);
    Optional<Booking> updateBooking(Long bookingId, Booking booking);
    Optional<Booking> getBookingById(Long bookingId);
    Set<Booking> getAllBookingsForUser(Long userId);

}
