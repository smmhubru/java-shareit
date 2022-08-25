package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class ItemRequest {
    @Id
    @Column(name = "request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
//    @NotNull
//    private LocalDateTime created;
}
