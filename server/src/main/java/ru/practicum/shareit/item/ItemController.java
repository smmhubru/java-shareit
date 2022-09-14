package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.validator.OnCreate;
import ru.practicum.shareit.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(path = "/items")
@Validated
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService, CommentService commentService) {
        this.itemService = itemService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("")
    @Validated(OnCreate.class)
    public ResponseEntity<?> createItem(
            HttpServletRequest request,
            @RequestBody Item item,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User owner = userService.getUser(userId);
        item.setOwner(owner);
        return ResponseEntity.ok(itemService.addItem(item));
    }

    @GetMapping("")
    public ResponseEntity<?> getAllItems(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(itemService.getAllItems(userId, from, size));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId) {
        ItemDto result = itemService.getItem(itemId);
        if (!Objects.equals(result.getOwner().getId(), userId)) {
            result.setLastBooking(null);
            result.setNextBooking(null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItem(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @PathParam("text") String text,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(itemService.searchItemByText(text, from, size));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<?> updateItem(
            HttpServletRequest request,
            @RequestBody Item item,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User itemOwner = itemService.getItem(itemId).getOwner();
        if (!Objects.equals(itemOwner.getId(), userId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permissions");
        return ResponseEntity.ok(itemService.updateItem(itemId, item));
    }

    @PostMapping("/{itemId}/comment")
    @Validated(OnCreate.class)
    public ResponseEntity<?> addComment(
            HttpServletRequest request,
            @RequestBody Comment comment,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        return ResponseEntity.ok(commentService.addComment(userId, itemId, comment));

    }
}
