package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                                    @Qualifier("persistentItemStorage") ItemStorage itemStorage,
                                    @Qualifier("persistentUserStorage") UserStorage userStorage) {
        this.bookingRepository = bookingRepository;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Optional<Booking> addBooking(BookingCreationDto booking) {
        ItemDto item = itemStorage.getItem(booking.getItemId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
        User booker = userStorage.getUser(booking.getBookerId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find booker")
        );
        if (!item.getAvailable()) return Optional.empty();
        Booking result = booking.toBooking(ItemMapper.toItem(item), booker);
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
            return Optional.of(bookingRepository.save(booking));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Booking> getBookingsByState(User user, BookingState state) {
        if (state.equals(BookingState.ALL)) {
            return bookingRepository.findAllByBookerOrderByStartDesc(user);
        }
        if (state.equals(BookingState.PAST)) {
            return bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now());
        }
        if (state.equals(BookingState.FUTURE)) {
           return bookingRepository.findAllByBookerAndStartAfterOrderByStartDesc(user, LocalDateTime.now());
        }
        if (state.equals(BookingState.CURRENT)) {
            return bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(user, LocalDateTime.now(),
                    LocalDateTime.now());
        }
        if (state.equals(BookingState.WAITING)) {
            return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
        }
        if (state.equals(BookingState.REJECTED)) {
            return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Booking> getBookingsByOwner(User user, BookingState state) {
        if (state.equals(BookingState.ALL)) {
            return bookingRepository.findAllByItemOwnerOrderByStartDesc(user);
        }
        if (state.equals(BookingState.PAST)) {
            return bookingRepository.findAllByItemOwnerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now());
        }
        if (state.equals(BookingState.FUTURE)) {
            return bookingRepository.findAllByItemOwnerAndStartAfterOrderByStartDesc(user, LocalDateTime.now());
        }
        if (state.equals(BookingState.CURRENT)) {
            return bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(user,
                    LocalDateTime.now(), LocalDateTime.now());
        }
        if (state.equals(BookingState.WAITING)) {
            return bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
        }
        if (state.equals(BookingState.REJECTED)) {
            return bookingRepository.findAllByItemOwnerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
        }
        return null;
    }

    @Override
    public Optional<Booking> getLastBookingByOwnerForItem(Item item) {
        return bookingRepository.findFirstByItemAndItemOwnerAndEndBeforeOrderByStartDesc(item, item.getOwner(), LocalDateTime.now());
    }

    @Override
    public Optional<Booking> getNextBookingByOwnerForItem(Item item) {
        return bookingRepository.findFirstByItemAndItemOwnerAndStartAfterOrderByStartDesc(item, item.getOwner(), LocalDateTime.now());
    }
}
