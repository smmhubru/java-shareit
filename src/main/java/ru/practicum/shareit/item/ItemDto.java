package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
public class ItemDto {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    @NonNull
    private User owner;
    @NonNull
    private int requestId;
}
