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
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;

@RestController
@Slf4j
@RequestMapping(path = "/items")
@Validated
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("")
    @Validated(OnCreate.class)
    public ResponseEntity<?> createItem(
            HttpServletRequest request,
            @Valid @RequestBody Item item,
            @RequestHeader(value = "X-Sharer-User-Id") int userId,
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
    public ResponseEntity<?> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable @Positive int itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchItem(
            @RequestHeader(value = "X-Sharer-User-Id") int userId, @PathParam("text") String text) {
        return ResponseEntity.ok(itemService.searchItem(text));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<?> updateItem(
            HttpServletRequest request,
            @Valid @RequestBody Item item,
            @RequestHeader(value = "X-Sharer-User-Id") int userId,
            @PathVariable @Positive int itemId,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User itemOwner = itemService.getItem(itemId).getOwner();
        if (itemOwner.getId() != userId) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permissions");
        return ResponseEntity.ok(itemService.updateItem(itemId, item));
    }
}
