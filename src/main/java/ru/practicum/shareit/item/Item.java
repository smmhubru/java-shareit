package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.OnCreate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name can't be blank", groups = OnCreate.class)
    @NotNull(message = "Name can't be null", groups = OnCreate.class)
    @Column
    private String name;
    @NotBlank(message = "Description can't be null", groups = OnCreate.class)
    @NotNull(message = "Description can't be null", groups = OnCreate.class)
    @Column
    private String description;
    @NotNull(message = "Available can't be null", groups = OnCreate.class)
    @Column
    private Boolean available;
    @ManyToOne
    private User owner;
    @ManyToOne
    private ItemRequest request;

    public Item(Long id, String name, String description, Boolean available, User owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
