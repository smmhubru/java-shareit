package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validator.OnCreate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @ManyToOne(targetEntity = ItemRequest.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", insertable = false, updatable = false)
    @JsonIgnore
    private ItemRequest request;
    @Column(name = "request_id")
    private Long requestId;
    @OneToMany
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private List<Comment> comments;

    public Item(Long id, String name, String description, Boolean available, User owner, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
