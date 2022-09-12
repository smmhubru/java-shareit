package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping("")
    public ResponseEntity<Object> createItemRequest(
            @Valid @RequestBody ItemRequestDto itemRequestDto,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Creating item request {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping("")
    public ResponseEntity<Object> getOwnItemRequest(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.info("Get own item request userId={}", userId);
        return itemRequestClient.getOwnItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @PathVariable @Positive Long requestId) {
        log.info("Get item request by id, userId={}, requestId={}", userId, requestId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Get all item requests, userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }
}
