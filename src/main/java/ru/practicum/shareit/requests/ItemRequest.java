package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private int id;
    @NonNull
    private String description;
    @NonNull
    private User requester;
    @NonNull
    private LocalDateTime created;
}
