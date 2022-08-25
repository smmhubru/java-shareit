package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("")
    public ResponseEntity<?> createBooking(HttpServletRequest request,
                                           @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @Valid @RequestBody BookingCreationDto booking,
                                           Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(bookingService.createBooking(userId, booking));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<?> approveBooking(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam(required = true) boolean approved) {
        return ResponseEntity.ok(bookingService.approveBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getBookingById(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                            @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(userId, bookingId));
    }

    @GetMapping("")
    public ResponseEntity<?> getBookingsByState(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        return ResponseEntity.ok(bookingService.getBookingsByState(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getBookingsByOwner(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        return ResponseEntity.ok(bookingService.getBookingsByOwner(userId, state));
    }
}
