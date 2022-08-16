package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

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

    public Booking createBooking(Long userId, Booking booking) {
        User user = userStorage.getUser(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find user")
        );
        Item item = itemStorage.getItem(booking.getItemId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        booking.setBookerId(userId);
        System.out.println("SET BOOKER ID: " + booking);
        return bookingStorage.addBooking(booking).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to add booking")
        );
    }
}
