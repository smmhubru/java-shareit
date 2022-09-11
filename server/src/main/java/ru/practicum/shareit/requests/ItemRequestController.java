package ru.practicum.shareit.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestSerivce) {
        this.itemRequestService = itemRequestSerivce;
    }

    @PostMapping("")
    public ResponseEntity<?> createItemRequest(
            HttpServletRequest request,
            @Valid @RequestBody ItemRequest itemRequest,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: {}", request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(itemRequestService.addItemRequest(userId, itemRequest));
    }

    @GetMapping("")
    public ResponseEntity<?> getOwnItemRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.getOwnItemRequests(userId));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<?> getItemRequestById(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @PathVariable @Positive Long requestId) {
        return ResponseEntity.ok(itemRequestService.getItemRequestById(userId, requestId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllItemRequests(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(itemRequestService.getAllItemRequests(userId, from, size));
    }
}
