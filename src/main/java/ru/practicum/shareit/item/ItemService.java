package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.requests.ItemRequestDto;
import ru.practicum.shareit.requests.ItemRequestStorage;

import java.util.List;

@Service
public class ItemService {
    private final ItemStorage itemStorage;
    private final ItemRequestStorage itemRequestStorage;

    @Autowired
    public ItemService(ItemStorage storage, ItemRequestStorage itemRequestStorage) {
        this.itemStorage = storage;
        this.itemRequestStorage = itemRequestStorage;
    }

    public Item addItem(Item item) {
        if (item.getRequest() != null) {
            ItemRequestDto itemRequest = itemRequestStorage.getItemRequestById(item.getRequestId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item request")
            );
        }
        return itemStorage.addItem(item).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Unable to add item")
        );
    }

    public ItemDto getItem(Long itemId) {
        return itemStorage.getItem(itemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
    }

    public List<ItemDto> getAllItems(Long userId) {
        return itemStorage.getAllItems(userId);
    }

    public Item updateItem(Long itemId, Item item) {
        return itemStorage.updateItem(itemId, item).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item")
        );
    }

    public List<Item> searchItemByText(String text) {
        return itemStorage.searchItem(text);
    }
}
