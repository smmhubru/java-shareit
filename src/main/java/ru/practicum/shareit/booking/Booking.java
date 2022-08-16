package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.validator.BookingDate;
import ru.practicum.shareit.validator.OnCreate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    @BookingDate(message = "Booking time can't be in past")
    private LocalDateTime start;
    @Column(name = "end_date")
    @BookingDate(message = "Booking time can't be in past")
    private LocalDateTime end;
    @NotBlank(message = "ItemId can't be blanc", groups = OnCreate.class)
    @NotNull(message = "ItemId can't be null", groups = OnCreate.class)
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "booker_id")
    private Long bookerId;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

