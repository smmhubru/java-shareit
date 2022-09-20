package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingShortDto {
    private Long id;
    private Long bookerId;
}
