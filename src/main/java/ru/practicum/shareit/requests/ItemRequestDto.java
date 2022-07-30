package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private int id;
    private String description;
    private User requester;
    private LocalDateTime created;
}
