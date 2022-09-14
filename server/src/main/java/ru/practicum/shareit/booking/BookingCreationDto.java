package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
public class BookingCreationDto {
    private Long id;
    @NonNull
    private LocalDateTime start;
    @NonNull
    private LocalDateTime end;
    @NonNull
    private Long itemId;
    private Long bookerId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    Booking toBooking(Item item, User booker) {
        Booking result = new Booking();
        result.setId(id);
        result.setStart(start);
        result.setEnd(end);
        result.setItem(item);
        result.setBooker(booker);
        result.setStatus(status);
        return result;
    }
}
