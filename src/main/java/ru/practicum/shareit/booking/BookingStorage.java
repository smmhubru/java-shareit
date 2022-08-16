package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface to interact with Booking Storage.
 */
public interface BookingStorage {
    Optional<Booking> addBooking(BookingCreationDto booking);
    Optional<Booking> updateBooking(Long bookingId, Booking booking);
    Optional<Booking> getBookingById(Long bookingId);
    Set<Booking> getAllBookingsForUser(Long userId);
    Optional<Booking> approveBooking(Booking booking, boolean approved);
    List<Booking> getBookingsByState(Long userId, BookingState state);
}
