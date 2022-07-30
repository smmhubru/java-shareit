package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserDto {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String email;
}
