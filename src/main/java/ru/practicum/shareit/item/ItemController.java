package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.validator.ValidationErrorBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> createItem(
            HttpServletRequest request,
            @Valid @RequestBody Item item,
            @RequestHeader(value = "X-Sharer-User-Id") String userId,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User owner = userService.getUser(Integer.parseInt(userId));
        item.setOwner(owner);
        System.out.println(item);
        return ResponseEntity.ok(itemService.addItem(item));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable int itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }
}
