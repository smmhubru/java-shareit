package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {
    private int id;
    @NotBlank(message = "Name can't be blank", groups = OnCreate.class)
    @NotNull(message = "Name can't be null", groups = OnCreate.class)
    private String name;
    @NotBlank(message = "Description can't be null", groups = OnCreate.class)
    @NotNull(message = "Description can't be null", groups = OnCreate.class)
    private String description;
    @NotNull(message = "Available can't be null", groups = OnCreate.class)
    private Boolean available;
    private User owner;
    private ItemRequest request;

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }
}
