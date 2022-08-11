package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.validator.OnCreate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name can't be blank", groups = OnCreate.class)
    @NotNull(message = "Name can't be null", groups = OnCreate.class)
    @Column
    private String name;
    @Email(message = "Email should be in right format")
    @NotBlank(message = "Email can't be blank", groups = OnCreate.class)
    @NotNull(message = "Email can't be null", groups = OnCreate.class)
    @Column
    private String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
