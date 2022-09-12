package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @MockBean
    BookingService bookingService;

    final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createBooking() throws Exception {
        BookingCreationDto request = new BookingCreationDto(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                42L
        );

        Booking booking = new Booking();
        booking.setId(request.getId());
        when(bookingService.createBooking(Mockito.anyLong(), any(BookingCreationDto.class))).thenReturn(booking);
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "42")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()));
    }

    @Test
    void approveBooking() throws Exception {
        Booking booking = new Booking();
        booking.setId(42L);
        when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(booking);
        mockMvc.perform(patch("/bookings/42")
                .header("X-Sharer-User-Id", "42")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()));
    }

    @Test
    void getBookingById() throws Exception {
        Booking booking = new Booking();
        booking.setId(42L);
        when(bookingService.getBookingById(Mockito.anyLong(),Mockito.anyLong())).thenReturn(booking);
        mockMvc.perform(get("/bookings/42")
                .header("X-Sharer-User-Id", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()));
    }

    @Test
    void getBookingsByState() throws Exception {
        List<Booking> bookings = List.of(new Booking(), new Booking());
        when(bookingService.getBookingsByState(
                Mockito.anyLong(), any(BookingState.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookings);
        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", "42")
                .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getBookingsByOwner() throws Exception {
        List<Booking> bookings = List.of(new Booking(), new Booking());
        when(bookingService.getBookingsByOwner(
                Mockito.anyLong(), any(BookingState.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookings);
        mockMvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", "42")
                .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}