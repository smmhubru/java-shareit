package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import java.util.Optional;
import java.util.Set;

@Component
@Qualifier("persistentBookingStorage")
public class PersistentBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public PersistentBookingStorage(BookingRepository bookingRepository,
                                    ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Optional<Booking> addBooking(Booking booking) {
        Optional<Item> item = itemRepository.findById(booking.getItemId());
        if (item.isEmpty()) return Optional.empty();
        if (!item.get().getAvailable()) return Optional.empty();
        booking.setStatus(BookingStatus.WAITING);
        try {
            System.out.println("TRY TO SAVE " + booking);
            bookingRepository.save(booking);
            return Optional.of(booking);
        } catch (Exception e) {
            System.out.println("UNABLE TO SAVE booking: " + booking + " " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Booking> updateBooking(Long bookingId, Booking booking) {
        return Optional.empty();
    }

    @Override
    public Optional<Booking> getBookingById(Long bookingId) {
        return Optional.empty();
    }

    @Override
    public Set<Booking> getAllBookingsForUser(Long userId) {
        return null;
    }
}
