package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

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

    List<Booking> getBookingsByState(User user, BookingState state, int from, int size);

    List<Booking> getBookingsByOwner(User user, BookingState state, int from, int size);

    Optional<Booking> getLastBookingByOwnerForItem(Item item);

    Optional<Booking> getNextBookingByOwnerForItem(Item item);

    boolean checkUserBookedItemInPast(User user, Item item);
}
