package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;
import java.util.Objects;

@Service
public class BookingService {
    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public BookingService(@Qualifier("persistentBookingStorage") BookingStorage bookingStorage,
                          @Qualifier("persistentItemStorage") ItemStorage itemStorage,
                          @Qualifier("persistentUserStorage") UserStorage userStorage) {
        this.bookingStorage = bookingStorage;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public Booking createBooking(Long userId, BookingCreationDto booking) {
        ItemDto item = itemStorage.getItem(booking.getItemId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can't book item you already own.");
        }
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
        ItemDto item = itemStorage.getItem(booking.getItem().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Only item owner can approve booking");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can approve booking with WAITING status");
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

    public List<Booking> getBookingsByState(Long userId, BookingState state, int from, int size) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return bookingStorage.getBookingsByState(user, state, from, size);
    }

    public List<Booking> getBookingsByOwner(Long userId, BookingState state, int from, int size) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        return bookingStorage.getBookingsByOwner(user, state, from, size);
    }
}
