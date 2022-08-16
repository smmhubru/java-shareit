package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.Optional;
import java.util.Set;

@Component
@Qualifier("persistentBookingStorage")
public class PersistentBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public PersistentBookingStorage(BookingRepository bookingRepository,
                                    ItemRepository itemRepository,
                                    @Qualifier("persistentItemStorage") ItemStorage itemStorage,
                                    @Qualifier("persistentUserStorage") UserStorage userStorage) {
        this.bookingRepository = bookingRepository;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Optional<Booking> addBooking(BookingCreationDto booking) {
        Item item = itemStorage.getItem(booking.getItemId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        User booker = userStorage.getUser(booking.getBookerId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find booker")
        );
        if (!item.getAvailable()) return Optional.empty();
        Booking result = booking.toBooking(item, booker);
        try {
            return Optional.of(bookingRepository.save(result));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Booking> updateBooking(Long bookingId, Booking booking) {
        return Optional.empty();
    }

    @Override
    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public Set<Booking> getAllBookingsForUser(Long userId) {
        return null;
    }

    @Override
    public Optional<Booking> approveBooking(Booking booking, boolean approved) {
        try {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }
            return Optional.of(booking);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
