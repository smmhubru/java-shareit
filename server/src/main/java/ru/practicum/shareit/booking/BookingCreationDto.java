package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.BookingDate;
import ru.practicum.shareit.validator.OnCreate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingCreationDto {
    private Long id;
    @NonNull
    @BookingDate(message = "Booking time can't be in past")
    private LocalDateTime start;
    @NonNull
    @BookingDate(message = "Booking time can't be in past")
    private LocalDateTime end;
    @NonNull
    @NotBlank(message = "ItemId can't be blanc", groups = OnCreate.class)
    @NotNull(message = "ItemId can't be null", groups = OnCreate.class)
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
