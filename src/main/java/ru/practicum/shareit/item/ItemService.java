package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;

@Service
public class ItemService {
    private final ItemStorage storage;

    @Autowired
    public ItemService(ItemStorage storage) {
        this.storage = storage;
    }

    public Item addItem(Item item) {
        return storage.addItem(item).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add item")
        );
    }

    public Item getItem(int itemId) {
        return storage.getItem(itemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
    }
}
