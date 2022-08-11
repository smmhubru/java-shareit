package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
public class BookingDto {
    @NonNull
    private int id;
    @NonNull
    private LocalDate start;
    @NonNull
    private LocalDate end;
    @NonNull
    private Item item;
    @NonNull
    private User booker;
    @NonNull
    private BookingStatus status;
}
