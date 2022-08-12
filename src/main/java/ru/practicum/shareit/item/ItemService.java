package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ItemService {
    private final ItemStorage storage;

    @Autowired
    public ItemService(@Qualifier("persistentItemStorage") ItemStorage storage) {
        this.storage = storage;
    }

    public Item addItem(Item item) {
        return storage.addItem(item).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add item")
        );
    }

    public Item getItem(Long itemId) {
        return storage.getItem(itemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
    }

    public List<Item> getAllItems(Long userId) {
        return storage.getAllItems(userId);
    }

    public Item updateItem(Long itemId, Item item) {
        return storage.updateItem(itemId, item).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
    }

    public List<Item> searchItemByText(String text) {
        return storage.searchItem(text);
    }
}
