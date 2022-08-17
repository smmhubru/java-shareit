package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerOrderByStartDesc(User booker);
    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime end);
    List<Booking> findAllByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime start);
    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);
    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);
    List<Booking> findAllByItemOwnerOrderByStartDesc(User owner);
    List<Booking> findAllByItemOwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime end);
    List<Booking> findAllByItemOwnerAndStartAfterOrderByStartDesc(User owner, LocalDateTime start);
    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User owner, LocalDateTime start, LocalDateTime end);
    List<Booking> findAllByItemOwnerAndStatusOrderByStartDesc(User owner, BookingStatus status);
    Optional<Booking> findFirstByItemAndItemOwnerAndEndBeforeOrderByStartDesc(Item item, User owner, LocalDateTime end);
    Optional<Booking> findFirstByItemAndItemOwnerAndStartAfterOrderByStartDesc(Item item, User owner, LocalDateTime start);
    Optional<Booking> findFirstByItemAndBookerAndEndBefore(Item item, User booker, LocalDateTime end);
}
