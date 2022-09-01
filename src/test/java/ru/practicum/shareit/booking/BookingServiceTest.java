package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private UserStorage userStorage;


    @Test
    void createBooking() {
        ItemDto itemDto = new ItemDto();
        itemDto.setOwner(new User(43L, "test user", "test@user.email"));
        when(itemStorage.getItem(Mockito.anyLong())).thenReturn(Optional.of(itemDto));
        when(bookingStorage.addBooking(any(BookingCreationDto.class))).thenReturn(Optional.of(new Booking()));
        Booking result = bookingService.createBooking(
                42L, new BookingCreationDto(LocalDateTime.now(), LocalDateTime.now(), 42L));
        assertInstanceOf(Booking.class, result);
    }

    @Test
    void approveBooking() {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(42L);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        when(userStorage.getUser(Mockito.anyLong()))
                .thenReturn(Optional.of(new User(42L, "testuser", "test@user.email")));
        when(bookingStorage.getBookingById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        ItemDto itemDto = new ItemDto();
        itemDto.setOwner(new User(42L, "owner user", "owner@mail.ru"));
        when(itemStorage.getItem(Mockito.anyLong())).thenReturn(Optional.of(itemDto));
        when(bookingStorage.approveBooking(any(Booking.class), Mockito.anyBoolean()))
                .thenReturn(Optional.of(new Booking()));
        Booking result = bookingService.approveBooking(42L, 42L, true);
        assertInstanceOf(Booking.class, result);
    }

    @Test
    void getBookingById() {
        Booking booking = new Booking();
        booking.setBooker(new User(42L, "username", "user@test.ru"));
        when(bookingStorage.getBookingById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Booking result = bookingService.getBookingById(42L, 42L);
        assertInstanceOf(Booking.class, result);
    }

    @Test
    void getBookingsByState() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        User user = new User(42L, "test", "test@mail.ru");
        when(userStorage.getUser(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingStorage.getBookingsByState(
                any(User.class), any(BookingState.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking1, booking2));
        List<Booking> result = bookingService.getBookingsByState(42L, BookingState.ALL, 42, 42);
        assertEquals(2, result.size());
    }

    @Test
    void getBookingsByOwner() {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        User user = new User(42L, "test", "test@mail.ru");
        when(userStorage.getUser(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.getBookingsByOwner(
                any(User.class), any(BookingState.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(booking1, booking2));
        List<Booking> result = bookingService.getBookingsByOwner(42L, BookingState.ALL, 42, 42);
        assertEquals(2, result.size());
    }
}