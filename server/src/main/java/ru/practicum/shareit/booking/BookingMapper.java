package ru.practicum.shareit.booking;

public class BookingMapper {
    public static BookingShortDto toShortDto(Booking booking) {
        if (booking == null) return null;
        return new BookingShortDto(booking.getId(), booking.getBooker().getId());
    }

}
