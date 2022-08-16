package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.Objects;

@Service
public class BookingService {
    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public BookingService(@Qualifier("persistentBookingStorage") BookingStorage bookingStorage,
                          @Qualifier("persistentItemStorage") ItemStorage itemStorage,
                          @Qualifier("persistentUserStorage") UserStorage userStorage) {
        this.bookingStorage = bookingStorage;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public Booking createBooking(Long userId, BookingCreationDto booking) {
        booking.setBookerId(userId);
        booking.setStatus(BookingStatus.WAITING);
        return bookingStorage.addBooking(booking).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to add booking")
        );
    }

    public Booking approveBooking(Long userId, Long bookingId, boolean approved) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Booking booking = bookingStorage.getBookingById(bookingId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find booking")
        );
        Item item = itemStorage.getItem(booking.getItem().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        if (userId != item.getOwner().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only item owner can approve booking");
        }
        return bookingStorage.approveBooking(booking, approved).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to approve booking")
        );
    }

    public Booking getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingStorage.getBookingById(bookingId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find booking")
        );
        if (Objects.equals(userId, booking.getBooker().getId()) || Objects.equals(userId, booking.getItem().getOwner().getId())) {
            return bookingStorage.getBookingById(bookingId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find booking by ID.")
            );
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Only requester or owner can see this");
        }
    }
}
