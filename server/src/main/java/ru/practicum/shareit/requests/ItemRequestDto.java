package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<Item> items;
}
