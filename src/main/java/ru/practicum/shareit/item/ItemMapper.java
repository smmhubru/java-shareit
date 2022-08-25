package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;

import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item, Booking lastBooking, Booking nextBooking) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest(),
                BookingMapper.toShortDto(lastBooking),
                BookingMapper.toShortDto(nextBooking),
                item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner(),
                itemDto.getRequest());
    }
}
