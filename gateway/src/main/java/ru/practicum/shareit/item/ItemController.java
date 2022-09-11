package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping("")
    public ResponseEntity<Object> createItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid CreateItemDto createItemDto) {
        log.info("Creating item {}, userId={}", createItemDto, userId);
        return itemClient.createItem(userId, createItemDto);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get all items, userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                     @PathVariable @Positive Long itemId) {
        log.info("Get item by id, userId={}, itemId={}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                        @PathParam("text") String text,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Search item by text={}, userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.searchItem(userId, text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @PathVariable @Positive Long itemId) {
        log.info("Update item {}, userId={}, itemId={}", itemDto, userId, itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDto commentDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                             @PathVariable @Positive Long itemId) {
        log.info("Add comment {}, userId={}, itemId={}", commentDto, userId, itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
