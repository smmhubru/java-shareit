package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.validator.OnCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private int id;
    @NotBlank(message = "Name can't be blank", groups = OnCreate.class)
    @NotNull(message = "Name can't be null", groups = OnCreate.class)
    private String name;
    @Email(message = "Email should be in right format")
    @NotBlank(message = "Email can't be blank", groups = OnCreate.class)
    @NotNull(message = "Email can't be null", groups = OnCreate.class)
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
