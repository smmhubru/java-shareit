package ru.practicum.shareit.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class ItemRequest {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @NotNull
    @Column
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    @JsonIgnore
    private User requester;
    @Column(name = "created_at")
    @JsonIgnore
    private LocalDateTime createdAt;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private List<Item> items;
}
